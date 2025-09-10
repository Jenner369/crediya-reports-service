package co.com.crediya.api.presentation.approvedloanapplicationmetrics.v1.handler;

import co.com.crediya.api.contract.RoleValidator;
import co.com.crediya.api.contract.RouteHandler;
import co.com.crediya.api.dto.approvedloanapplicationmetrics.ApprovedLoanApplicationReportDTO;
import co.com.crediya.api.dto.common.ErrorResponseDTO;
import co.com.crediya.model.role.enums.Roles;
import co.com.crediya.usecase.getapprovedloanapplicationcount.GetApprovedLoanApplicationCountUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ApprovedLoanApplicationReportHandlerV1 implements RouteHandler {
    private final GetApprovedLoanApplicationCountUseCase getApprovedLoanApplicationCountUseCase;
    private final RoleValidator roleValidator;

    @Override
    @Operation(
            tags = {"Reports API"},
            summary = "Obtener reporte de solicitudes de préstamo aprobadas",
            description = "Permite obtener un reporte con métricas de las solicitudes de préstamo aprobadas."
    )
    @ApiResponse(responseCode = "200", description = "Reporte generado exitosamente",
            content = @Content(schema = @Schema(implementation = ApprovedLoanApplicationReportDTO.class)))
    @ApiResponse(responseCode = "401", description = "No autorizado",
            content = @Content(schema = @Schema))
    @ApiResponse(responseCode = "403", description = "Prohibido",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    public Mono<ServerResponse> handle(ServerRequest request) {
        return roleValidator.validateRole(Roles.ADMIN)
                .then(generateReport())
                .flatMap(reportDTO ->
                        ServerResponse.ok().bodyValue(reportDTO)
                );
    }

    private Mono<ApprovedLoanApplicationReportDTO> generateReport() {
        return getApprovedLoanApplicationCountUseCase.execute(null)
                .map(count -> ApprovedLoanApplicationReportDTO.builder()
                        .approvedLoanCount(count)
                        .build()
                );
    }
}
