package co.com.crediya.model.approvedloanrequestmetric.enums;

import co.com.crediya.model.approvedloanrequestmetric.ApprovedLoanRequestMetric;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ApprovedLoanRequestMetrics {

    APPROVED_LOAN_COUNT("approved_loan_count"),
    APPROVED_LOAN_AMOUNT("approved_loan_amount");

    private final String code;

    public static ApprovedLoanRequestMetric createCountInitialMetric() {
        return ApprovedLoanRequestMetric.builder()
                .code(APPROVED_LOAN_COUNT.getCode())
                .value("0")
                .build();
    }

    public static ApprovedLoanRequestMetric createAmountInitialMetric() {
        return ApprovedLoanRequestMetric.builder()
                .code(APPROVED_LOAN_AMOUNT.getCode())
                .value("0")
                .build();
    }
}
