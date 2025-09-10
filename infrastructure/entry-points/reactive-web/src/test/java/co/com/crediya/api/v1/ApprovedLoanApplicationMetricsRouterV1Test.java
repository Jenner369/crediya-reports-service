package co.com.crediya.api.v1;

import co.com.crediya.api.authentication.filter.AuthUserDetails;
import co.com.crediya.api.contract.RoleValidator;
import co.com.crediya.api.dto.approvedloanapplicationmetrics.ApprovedLoanApplicationReportDTO;
import co.com.crediya.api.presentation.approvedloanapplicationmetrics.v1.handler.ApprovedLoanApplicationReportHandlerV1;
import co.com.crediya.api.presentation.approvedloanapplicationmetrics.v1.ApprovedLoanApplicationMetricsRouterV1;
import co.com.crediya.model.role.enums.Roles;
import co.com.crediya.usecase.getapprovedloanapplicationamount.GetApprovedLoanApplicationAmountUseCase;
import co.com.crediya.usecase.getapprovedloanapplicationcount.GetApprovedLoanApplicationCountUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockAuthentication;

@ContextConfiguration(classes = {
        ApprovedLoanApplicationMetricsRouterV1.class,
        ApprovedLoanApplicationReportHandlerV1.class
})
@WebFluxTest
class ApprovedLoanApplicationMetricsRouterV1Test {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private GetApprovedLoanApplicationCountUseCase getApprovedLoanApplicationCountUseCase;

    @MockitoBean
    private GetApprovedLoanApplicationAmountUseCase getApprovedLoanApplicationAmountUseCase;

    @MockitoBean
    private RoleValidator roleValidator;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public WebProperties.Resources webPropertiesResources() {
            return new WebProperties.Resources();
        }
    }

    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
            return http
                    .csrf(ServerHttpSecurity.CsrfSpec::disable)
                    .authorizeExchange(ex -> ex.anyExchange().permitAll())
                    .build();
        }
    }

    private AuthUserDetails authUser;

    @BeforeEach
    void setupAuthUser() {
        authUser = new AuthUserDetails(
                UUID.randomUUID().toString(),
                "jenner@crediya.com",
                "fake-password",
                List.of(new SimpleGrantedAuthority(Roles.ADMIN.getId().toString()))
        );
    }

    @Test
    void testGetApprovedLoanApplicationCount() {
        when(getApprovedLoanApplicationCountUseCase.execute(null))
                .thenReturn(Mono.just(10L));
        when(getApprovedLoanApplicationAmountUseCase.execute(null))
                .thenReturn(Mono.just(BigDecimal.TEN));
        when(roleValidator.validateRole(any(Roles.class))).thenReturn(Mono.empty());

        webTestClient.mutateWith(mockAuthentication(
                new UsernamePasswordAuthenticationToken(authUser, authUser.getPassword(), authUser.getAuthorities())
        ))
                .get()
                .uri("/api/v1/reportes")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ApprovedLoanApplicationReportDTO.class)
                .value(report -> {
                    Assertions.assertThat(report.getApprovedLoanCount()).isEqualTo(10L);
                    Assertions.assertThat(report.getApprovedLoanAmount()).isEqualTo(BigDecimal.TEN);
                });
    }
}
