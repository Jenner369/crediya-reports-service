package co.com.crediya.usecase.getapprovedloanapplicationamount;

import co.com.crediya.model.approvedloanrequestmetric.ApprovedLoanRequestMetric;
import co.com.crediya.model.approvedloanrequestmetric.enums.ApprovedLoanRequestMetrics;
import co.com.crediya.model.approvedloanrequestmetric.gateways.ApprovedLoanRequestMetricRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetApprovedLoanApplicationAmountUseCaseTest {
    @Mock
    private ApprovedLoanRequestMetricRepository repository;

    @InjectMocks
    private GetApprovedLoanApplicationAmountUseCase useCase;

    @Test
    void shouldReturnApprovedLoanApplicationAmount() {
        when(repository.findByMetricCode(anyString()))
                .thenReturn(Mono.just(new ApprovedLoanRequestMetric(
                        ApprovedLoanRequestMetrics.APPROVED_LOAN_AMOUNT.getCode(),
                        "5")
                ));

        StepVerifier.create(useCase.execute(null))
                .expectNext(new BigDecimal("5"))
                .verifyComplete();

        verify(repository).findByMetricCode(
                ApprovedLoanRequestMetrics.APPROVED_LOAN_AMOUNT.getCode()
        );
    }

    @Test
    void shouldReturnZeroWhenMetricNotFound() {
        when(repository.findByMetricCode(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(null))
                .expectNext(BigDecimal.ZERO)
                .verifyComplete();

        verify(repository).findByMetricCode(
                ApprovedLoanRequestMetrics.APPROVED_LOAN_AMOUNT.getCode()
        );
    }

    @Test
    void shouldReturnZeroWhenValueIsNotANumber() {
        when(repository.findByMetricCode(anyString()))
                .thenReturn(Mono.just(new ApprovedLoanRequestMetric(
                        ApprovedLoanRequestMetrics.APPROVED_LOAN_AMOUNT.getCode(),
                        "invalid_number")
                ));

        StepVerifier.create(useCase.execute(null))
                .expectNext(BigDecimal.ZERO)
                .verifyComplete();

        verify(repository).findByMetricCode(
                ApprovedLoanRequestMetrics.APPROVED_LOAN_AMOUNT.getCode()
        );
    }
}
