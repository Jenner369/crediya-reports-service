package co.com.crediya.metricsnotifications.scheduler;

import co.com.crediya.usecase.getapprovedloanapplicationamount.GetApprovedLoanApplicationAmountUseCase;
import co.com.crediya.usecase.getapprovedloanapplicationcount.GetApprovedLoanApplicationCountUseCase;
import co.com.crediya.usecase.sendloanapplicationapprovedmetricsnotification.SendLoanApplicationApprovedMetricsNotificationUseCase;
import co.com.crediya.usecase.sendloanapplicationapprovedmetricsnotification.SendLoanApplicationApprovedMetricsNotificationUseCaseInput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoanRequestsApprovedMetricsNotificationScheduler {
    private final GetApprovedLoanApplicationAmountUseCase getApprovedLoanApplicationAmountUseCase;
    private final GetApprovedLoanApplicationCountUseCase getApprovedLoanApplicationCountUseCase;
    private final SendLoanApplicationApprovedMetricsNotificationUseCase sendNotificationUseCase;

    @Scheduled(cron = "0 0 20 * * *", zone = "${app.tz}") // At 8:00 PM daily using app timezone
    public void publishDailyReport() {
        Mono.zip(
                        getApprovedLoanApplicationCountUseCase.execute(null),
                        getApprovedLoanApplicationAmountUseCase.execute(null)
                ).flatMap(tuple -> {
                    var input = new SendLoanApplicationApprovedMetricsNotificationUseCaseInput(
                            tuple.getT1(),
                            tuple.getT2()
                    );

                    return sendNotificationUseCase.execute(input);
                }).doOnSuccess(v -> log.info("[metrics-notifications] Daily approved loans report published successfully."))
                .doOnError(e -> log.error("[metrics-notifications] Error publishing daily approved loans report", e))
                .subscribe();
    }
}
