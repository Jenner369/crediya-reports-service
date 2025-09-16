package co.com.crediya.metricsnotifications.scheduler;


import co.com.crediya.usecase.getapprovedloanapplicationamount.GetApprovedLoanApplicationAmountUseCase;
import co.com.crediya.usecase.getapprovedloanapplicationcount.GetApprovedLoanApplicationCountUseCase;
import co.com.crediya.usecase.sendloanapplicationapprovedmetricsnotification.SendLoanApplicationApprovedMetricsNotificationUseCase;
import co.com.crediya.usecase.sendloanapplicationapprovedmetricsnotification.SendLoanApplicationApprovedMetricsNotificationUseCaseInput;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanRequestsApprovedMetricsNotificationSchedulerTest {
    @Mock
    private GetApprovedLoanApplicationAmountUseCase amountUseCase;

    @Mock
    private GetApprovedLoanApplicationCountUseCase countUseCase;

    @Mock
    private SendLoanApplicationApprovedMetricsNotificationUseCase sendNotificationUseCase;

    @InjectMocks
    private LoanRequestsApprovedMetricsNotificationScheduler scheduler;

    @Test
    void testPublishDailyReport() {
        when(countUseCase.execute(any())).thenReturn(Mono.just(5L));
        when(amountUseCase.execute(any())).thenReturn(Mono.just(new BigDecimal("100000")));
        when(sendNotificationUseCase.execute(any())).thenReturn(Mono.empty());

        var result = Mono.fromRunnable(() -> scheduler.publishDailyReport());
        StepVerifier.create(result).verifyComplete();

        verify(sendNotificationUseCase).execute(
                new SendLoanApplicationApprovedMetricsNotificationUseCaseInput(
                        5L,
                        new BigDecimal("100000")
                )
        );
    }
}
