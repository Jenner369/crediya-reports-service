package co.com.crediya.model.approvedloanrequestmetric.gateways;

import co.com.crediya.model.approvedloanrequestmetric.ApprovedLoanRequestMetric;
import reactor.core.publisher.Mono;

public interface ApprovedLoanRequestMetricRepository {
    Mono<ApprovedLoanRequestMetric> findByMetricCode(String code);
    Mono<ApprovedLoanRequestMetric> saveMetric(ApprovedLoanRequestMetric metric);
}
