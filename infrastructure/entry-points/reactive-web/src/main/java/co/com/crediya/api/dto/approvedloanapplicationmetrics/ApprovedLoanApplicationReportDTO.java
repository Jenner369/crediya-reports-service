package co.com.crediya.api.dto.approvedloanapplicationmetrics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Reporte de solicitudes de préstamo aprobadas")
public class ApprovedLoanApplicationReportDTO {
    @Schema(description = "Cantidad de solicitudes de préstamo aprobadas", example = "150")
    private Long approvedLoanCount;

    @Schema(description = "Monto total aprobado en solicitudes de préstamo", example = "250000.75")
    private BigDecimal approvedLoanAmount;
}