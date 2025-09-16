package co.com.crediya.usecase.sendloanapplicationapprovedmetricsnotification;

import java.math.BigDecimal;

public record SendLoanApplicationApprovedMetricsNotificationUseCaseInput(
        Long approvedLoanCount,
        BigDecimal approvedLoanAmount
) {
}
