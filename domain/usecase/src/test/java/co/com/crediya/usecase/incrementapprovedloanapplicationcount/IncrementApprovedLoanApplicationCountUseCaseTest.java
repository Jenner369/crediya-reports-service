package co.com.crediya.usecase.incrementapprovedloanapplicationcount;

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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IncrementApprovedLoanApplicationCountUseCaseTest {
    @Mock
    private ApprovedLoanRequestMetricRepository approvedLoanRequestMetricRepository;

    @InjectMocks
    private IncrementApprovedLoanApplicationCountUseCase useCase;

    @Test
    void shouldIncrementApprovedLoanApplicationCountWhenNotExists() {
        when(approvedLoanRequestMetricRepository.findByMetricCode(anyString()))
                .thenReturn(Mono.empty());
        when(approvedLoanRequestMetricRepository.saveMetric(any()))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(useCase.execute(null))
                .expectNextMatches(metric -> metric.getValue().equals("1"))
                .verifyComplete();

        verify(approvedLoanRequestMetricRepository).findByMetricCode(anyString());
        verify(approvedLoanRequestMetricRepository).saveMetric(any());
    }

    @Test
    void shouldIncrementApprovedLoanApplicationCountWhenExists() {
        when(approvedLoanRequestMetricRepository.findByMetricCode(anyString()))
                .thenReturn(Mono.just(new ApprovedLoanRequestMetric(
                        ApprovedLoanRequestMetrics.APPROVED_LOAN_COUNT.getCode(),
                        "20")
                ));
        when(approvedLoanRequestMetricRepository.saveMetric(any()))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(useCase.execute(null))
                .expectNextMatches(metric -> metric.getValue().equals("21"))
                .verifyComplete();

        verify(approvedLoanRequestMetricRepository).findByMetricCode(anyString());
        verify(approvedLoanRequestMetricRepository).saveMetric(any());
    }

    @Test
    void shouldReturnZeroWhenValueIsNotANumber() {
        when(approvedLoanRequestMetricRepository.findByMetricCode(anyString()))
                .thenReturn(Mono.just(new ApprovedLoanRequestMetric(
                        ApprovedLoanRequestMetrics.APPROVED_LOAN_COUNT.getCode(),
                        "notANumber")
                ));
        when(approvedLoanRequestMetricRepository.saveMetric(any()))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(useCase.execute(null))
                .expectNextMatches(metric -> metric.getValue().equals("1"))
                .verifyComplete();

        verify(approvedLoanRequestMetricRepository).findByMetricCode(anyString());
        verify(approvedLoanRequestMetricRepository).saveMetric(any());
    }
}
