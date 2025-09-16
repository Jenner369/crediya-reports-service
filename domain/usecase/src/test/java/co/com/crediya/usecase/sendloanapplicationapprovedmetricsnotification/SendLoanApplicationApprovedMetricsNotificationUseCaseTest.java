package co.com.crediya.usecase.sendloanapplicationapprovedmetricsnotification;

import co.com.crediya.model.approvedloanrequestmetric.gateways.ApprovedLoanRequestMetricsEventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SendLoanApplicationApprovedMetricsNotificationUseCaseTest {
    @Mock
    private ApprovedLoanRequestMetricsEventPublisher publisher;

    @InjectMocks
    private SendLoanApplicationApprovedMetricsNotificationUseCase useCase;

    @Test
    void execute_ShouldPublishNotification() {
        var approvedLoanCount = 5L;
        var approvedLoanAmount = new java.math.BigDecimal("10000.00");
        var input = new SendLoanApplicationApprovedMetricsNotificationUseCaseInput(
                approvedLoanCount,
                approvedLoanAmount
        );

        when(publisher.publishNotification(any())).thenReturn(Mono.empty());

        Mono<Void> result = useCase.execute(input);

        StepVerifier.create(result)
                .verifyComplete();

        verify(publisher, times(1)).publishNotification(any());
    }
}
