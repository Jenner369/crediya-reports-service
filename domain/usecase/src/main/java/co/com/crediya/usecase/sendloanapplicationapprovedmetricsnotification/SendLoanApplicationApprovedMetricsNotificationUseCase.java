package co.com.crediya.usecase.sendloanapplicationapprovedmetricsnotification;

import co.com.crediya.contract.ReactiveUseCase;
import co.com.crediya.model.approvedloanrequestmetric.events.ApprovedLoanRequestMetricsNotificationEvent;
import co.com.crediya.model.approvedloanrequestmetric.gateways.ApprovedLoanRequestMetricsEventPublisher;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Date;

@RequiredArgsConstructor
public class SendLoanApplicationApprovedMetricsNotificationUseCase
        implements ReactiveUseCase<SendLoanApplicationApprovedMetricsNotificationUseCaseInput, Mono<Void>> {
    private final ApprovedLoanRequestMetricsEventPublisher publisher;

    @Override
    public Mono<Void> execute(SendLoanApplicationApprovedMetricsNotificationUseCaseInput input) {
        return mapToEvent(input)
                .flatMap(publisher::publishNotification);
    }

    private Mono<ApprovedLoanRequestMetricsNotificationEvent> mapToEvent(
            SendLoanApplicationApprovedMetricsNotificationUseCaseInput input
    ) {
        return Mono.just(new ApprovedLoanRequestMetricsNotificationEvent(
                input.approvedLoanCount(),
                input.approvedLoanAmount(),
                new Date()
        ));
    }
}
