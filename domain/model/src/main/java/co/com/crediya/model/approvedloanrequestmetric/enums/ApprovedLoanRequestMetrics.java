package co.com.crediya.model.approvedloanrequestmetric.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ApprovedLoanRequestMetrics {

    APPROVED_LOAN_COUNT("approved_loan_count");

    private final String code;
}
