package co.com.crediya.usecase.getapprovedloanapplicationamount;

import co.com.crediya.contract.ReactiveUseCase;
import co.com.crediya.model.approvedloanrequestmetric.ApprovedLoanRequestMetric;
import co.com.crediya.model.approvedloanrequestmetric.enums.ApprovedLoanRequestMetrics;
import co.com.crediya.model.approvedloanrequestmetric.gateways.ApprovedLoanRequestMetricRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class GetApprovedLoanApplicationAmountUseCase
    implements ReactiveUseCase<Void, Mono<BigDecimal>>
{
    private final ApprovedLoanRequestMetricRepository repository;

    @Override
    public Mono<BigDecimal> execute(Void input) {
        return repository.findByMetricCode(ApprovedLoanRequestMetrics.APPROVED_LOAN_AMOUNT.getCode())
                .flatMap(this::mapValueToBigDecimal)
                .defaultIfEmpty(BigDecimal.ZERO);
    }

    private Mono<BigDecimal> mapValueToBigDecimal(ApprovedLoanRequestMetric metric) {
        return Mono.just(metric.getValue())
            .map(BigDecimal::new)
            .onErrorReturn(BigDecimal.ZERO);
    }
}
