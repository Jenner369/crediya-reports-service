package co.com.crediya.model.approvedloanrequestmetric.events;

import java.math.BigDecimal;
import java.util.Date;

public record ApprovedLoanRequestMetricsNotificationEvent(
        Long approvedLoanCount,
        BigDecimal approvedLoanAmount,
        Date timestamp
) {
}
