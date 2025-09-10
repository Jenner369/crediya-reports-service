package co.com.crediya.usecase.getapprovedloanapplicationcount;

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
class GetApprovedLoanApplicationCountUseCaseTest {
    @Mock
    private ApprovedLoanRequestMetricRepository repository;

    @InjectMocks
    private GetApprovedLoanApplicationCountUseCase useCase;

    @Test
    void shouldReturnApprovedLoanApplicationCount() {
        when(repository.findByMetricCode(anyString()))
                .thenReturn(Mono.just(new ApprovedLoanRequestMetric(
                        ApprovedLoanRequestMetrics.APPROVED_LOAN_COUNT.getCode(),
                        "5")
                ));

        StepVerifier.create(useCase.execute(null))
                .expectNext(5L)
                .verifyComplete();

        verify(repository).findByMetricCode(
                ApprovedLoanRequestMetrics.APPROVED_LOAN_COUNT.getCode()
        );
    }

    @Test
    void shouldReturnZeroWhenMetricNotFound() {
        when(repository.findByMetricCode(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(null))
                .expectNext(0L)
                .verifyComplete();

        verify(repository).findByMetricCode(
                ApprovedLoanRequestMetrics.APPROVED_LOAN_COUNT.getCode()
        );
    }

    @Test
    void shouldReturnZeroWhenValueIsNotANumber() {
        when(repository.findByMetricCode(anyString()))
                .thenReturn(Mono.just(new ApprovedLoanRequestMetric(
                        ApprovedLoanRequestMetrics.APPROVED_LOAN_COUNT.getCode(),
                        "invalid_number")
                ));

        StepVerifier.create(useCase.execute(null))
                .expectNext(0L)
                .verifyComplete();

        verify(repository).findByMetricCode(
                ApprovedLoanRequestMetrics.APPROVED_LOAN_COUNT.getCode()
        );
    }
}
