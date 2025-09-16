package co.com.crediya.model.approvedloanrequestmetric.gateways;

import co.com.crediya.model.approvedloanrequestmetric.events.ApprovedLoanRequestMetricsNotificationEvent;
import reactor.core.publisher.Mono;

public interface ApprovedLoanRequestMetricsEventPublisher {
    Mono<Void> publishNotification(ApprovedLoanRequestMetricsNotificationEvent event);
}
