package co.com.crediya.usecase.incrementapprovedloanapplicationamount;

import co.com.crediya.model.approvedloanrequestmetric.ApprovedLoanRequestMetric;
import co.com.crediya.model.approvedloanrequestmetric.enums.ApprovedLoanRequestMetrics;
import co.com.crediya.model.approvedloanrequestmetric.gateways.ApprovedLoanRequestMetricRepository;
import co.com.crediya.usecase.incrementapprovedloanapplicationcount.IncrementApprovedLoanApplicationCountUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IncrementApprovedLoanApplicationAmountUseCaseTest {
    @Mock
    private ApprovedLoanRequestMetricRepository approvedLoanRequestMetricRepository;

    @InjectMocks
    private IncrementApprovedLoanApplicationAmountUseCase useCase;

    private final BigDecimal amount = BigDecimal.TEN;

    @Test
    void shouldIncrementApprovedLoanApplicationAmountWhenNotExists() {
        when(approvedLoanRequestMetricRepository.findByMetricCode(anyString()))
                .thenReturn(Mono.empty());
        when(approvedLoanRequestMetricRepository.saveMetric(any()))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(useCase.execute(amount))
                .expectNextMatches(metric
                        -> amount.equals(new BigDecimal(metric.getValue()))
                )
                .verifyComplete();

        verify(approvedLoanRequestMetricRepository).findByMetricCode(anyString());
        verify(approvedLoanRequestMetricRepository).saveMetric(any());
    }

    @Test
    void shouldIncrementApprovedLoanApplicationAmountWhenExists() {
        when(approvedLoanRequestMetricRepository.findByMetricCode(anyString()))
                .thenReturn(Mono.just(new ApprovedLoanRequestMetric(
                        ApprovedLoanRequestMetrics.APPROVED_LOAN_COUNT.getCode(),
                        "40000")
                ));
        when(approvedLoanRequestMetricRepository.saveMetric(any()))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(useCase.execute(amount))
                .expectNextMatches(metric -> metric.getValue().equals("40010"))
                .verifyComplete();

        verify(approvedLoanRequestMetricRepository).findByMetricCode(anyString());
        verify(approvedLoanRequestMetricRepository).saveMetric(any());
    }

    @Test
    void shouldReturnAmountWhenValueIsNotANumber() {
        when(approvedLoanRequestMetricRepository.findByMetricCode(anyString()))
                .thenReturn(Mono.just(new ApprovedLoanRequestMetric(
                        ApprovedLoanRequestMetrics.APPROVED_LOAN_COUNT.getCode(),
                        "notANumber")
                ));
        when(approvedLoanRequestMetricRepository.saveMetric(any()))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(useCase.execute(amount))
                .expectNextMatches(metric
                        -> amount.equals(new BigDecimal(metric.getValue()))
                )
                .verifyComplete();

        verify(approvedLoanRequestMetricRepository).findByMetricCode(anyString());
        verify(approvedLoanRequestMetricRepository).saveMetric(any());
    }
}
