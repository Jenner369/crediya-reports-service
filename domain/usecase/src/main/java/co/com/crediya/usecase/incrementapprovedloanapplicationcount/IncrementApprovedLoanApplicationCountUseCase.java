package co.com.crediya.usecase.incrementapprovedloanapplicationcount;

import co.com.crediya.contract.ReactiveUseCase;
import co.com.crediya.model.approvedloanrequestmetric.ApprovedLoanRequestMetric;
import co.com.crediya.model.approvedloanrequestmetric.enums.ApprovedLoanRequestMetrics;
import co.com.crediya.model.approvedloanrequestmetric.gateways.ApprovedLoanRequestMetricRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class IncrementApprovedLoanApplicationCountUseCase
        implements ReactiveUseCase<Void, Mono<ApprovedLoanRequestMetric>>
{
    private final ApprovedLoanRequestMetricRepository repository;

    @Override
    public Mono<ApprovedLoanRequestMetric> execute(Void input) {
        return repository.findByMetricCode(ApprovedLoanRequestMetrics.APPROVED_LOAN_COUNT.getCode())
                .switchIfEmpty(Mono.just(createInitialMetric()))
                .flatMap(this::incrementCount);
    }

    private ApprovedLoanRequestMetric createInitialMetric() {
        return ApprovedLoanRequestMetric.builder()
                .code(ApprovedLoanRequestMetrics.APPROVED_LOAN_COUNT.getCode())
                .value("0")
                .build();
    }

    private Mono<ApprovedLoanRequestMetric> incrementCount(ApprovedLoanRequestMetric metric) {
        return Mono.just(metric.getValue())
                .map(Long::parseLong)
                .onErrorReturn(0L)
                .map(count -> count + 1L)
                .map(newCount -> {
                    metric.setValue(String.valueOf(newCount));

                    return metric;
                })
                .flatMap(repository::saveMetric);
    }
}

