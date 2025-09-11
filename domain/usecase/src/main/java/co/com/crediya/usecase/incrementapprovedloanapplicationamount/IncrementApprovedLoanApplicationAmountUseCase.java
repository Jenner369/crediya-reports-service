package co.com.crediya.usecase.incrementapprovedloanapplicationamount;

import co.com.crediya.contract.ReactiveUseCase;
import co.com.crediya.model.approvedloanrequestmetric.ApprovedLoanRequestMetric;
import co.com.crediya.model.approvedloanrequestmetric.enums.ApprovedLoanRequestMetrics;
import co.com.crediya.model.approvedloanrequestmetric.gateways.ApprovedLoanRequestMetricRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class IncrementApprovedLoanApplicationAmountUseCase
    implements ReactiveUseCase<BigDecimal, Mono<ApprovedLoanRequestMetric>>

{
    private final ApprovedLoanRequestMetricRepository repository;

    @Override
    public Mono<ApprovedLoanRequestMetric> execute(BigDecimal amount) {
        return repository.findByMetricCode(ApprovedLoanRequestMetrics.APPROVED_LOAN_AMOUNT.getCode())
                .switchIfEmpty(Mono.just(ApprovedLoanRequestMetrics.createAmountInitialMetric()))
                .flatMap(metric -> incrementAmount(metric, amount));
    }

    private Mono<ApprovedLoanRequestMetric> incrementAmount(ApprovedLoanRequestMetric metric, BigDecimal amount) {
        return Mono.just(metric.getValue())
                .map(BigDecimal::new)
                .onErrorReturn(BigDecimal.ZERO)
                .map(currentAmount -> currentAmount.add(amount))
                .map(newAmount -> {
                    metric.setValue(newAmount.toString());

                    return metric;
                })
                .flatMap(repository::saveMetric);
    }
}
