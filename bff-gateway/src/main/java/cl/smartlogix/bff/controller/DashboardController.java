package cl.smartlogix.bff.controller;

import cl.smartlogix.bff.dto.DashboardResponseDTO;
import cl.smartlogix.bff.facade.LogisticaFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Agregación BFF")
public class DashboardController {

    private final LogisticaFacade logisticaFacade;

    @GetMapping("/dashboard")
    @Operation(summary = "Dashboard agregado: pedido, productos y envío")
    public ResponseEntity<DashboardResponseDTO> dashboard() {
        return ResponseEntity.ok(logisticaFacade.obtenerDashboard());
    }
}
