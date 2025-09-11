package co.com.crediya.usecase.getapprovedloanapplicationcount;

import co.com.crediya.contract.ReactiveUseCase;
import co.com.crediya.model.approvedloanrequestmetric.ApprovedLoanRequestMetric;
import co.com.crediya.model.approvedloanrequestmetric.enums.ApprovedLoanRequestMetrics;
import co.com.crediya.model.approvedloanrequestmetric.gateways.ApprovedLoanRequestMetricRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GetApprovedLoanApplicationCountUseCase
        implements ReactiveUseCase<Void, Mono<Long>>
{
    private final ApprovedLoanRequestMetricRepository repository;

    @Override
    public Mono<Long> execute(Void input) {
        return repository.findByMetricCode(ApprovedLoanRequestMetrics.APPROVED_LOAN_COUNT.getCode())
                .flatMap(this::mapValueToLong)
                .defaultIfEmpty(0L);
    }

    private Mono<Long> mapValueToLong(ApprovedLoanRequestMetric metric) {
        return Mono.just(metric.getValue())
                .map(Long::parseLong)
                .onErrorReturn(0L);
    }
}
