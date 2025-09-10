package co.com.crediya.sqs.listener.dto;

import java.math.BigDecimal;
import java.util.Date;

public record LoanApplicationApprovedEventDTO(
        String id,
        BigDecimal amount,
        Date approvedAt
) {
}
