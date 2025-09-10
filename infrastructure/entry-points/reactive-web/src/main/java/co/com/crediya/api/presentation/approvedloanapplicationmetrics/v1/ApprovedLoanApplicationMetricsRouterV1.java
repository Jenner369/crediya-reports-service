package co.com.crediya.api.presentation.approvedloanapplicationmetrics.v1;

import co.com.crediya.api.presentation.approvedloanapplicationmetrics.v1.handler.ApprovedLoanApplicationReportHandlerV1;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class ApprovedLoanApplicationMetricsRouterV1 {
    @Bean
    @RouterOperations({
        @RouterOperation(
            path = "/api/v1/reportes",
            beanClass = ApprovedLoanApplicationReportHandlerV1.class,
            beanMethod = "handle",
            method = RequestMethod.GET
        )
    })
    public RouterFunction<ServerResponse> routerFunction(
            ApprovedLoanApplicationReportHandlerV1 approvedLoanApplicationReportHandlerV1
    ) {
        return RouterFunctions
            .route()
                .path("/api/v1/reportes", builder -> builder
                        .GET("", approvedLoanApplicationReportHandlerV1::handle)
                )
            .build();
    }
}
