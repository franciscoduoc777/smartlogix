package cl.smartlogix.envios.controller;

import cl.smartlogix.envios.dto.EnvioRequestDTO;
import cl.smartlogix.envios.dto.EnvioResponseDTO;
import cl.smartlogix.envios.model.EstadoEnvio;
import cl.smartlogix.envios.service.EnvioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/envios")
@RequiredArgsConstructor
@Tag(name = "Envíos", description = "API de gestión de envíos")
public class EnvioController {

    private final EnvioService envioService;

    @GetMapping
    @Operation(summary = "Listar envíos")
    public ResponseEntity<List<EnvioResponseDTO>> listar() {
        return ResponseEntity.ok(envioService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener envío por id")
    public ResponseEntity<EnvioResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(envioService.obtenerPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear envío para pedido aprobado")
    public ResponseEntity<EnvioResponseDTO> crear(@Valid @RequestBody EnvioRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(envioService.crear(request));
    }

    @PutMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado del envío")
    public ResponseEntity<EnvioResponseDTO> actualizarEstado(
            @PathVariable Long id,
            @RequestParam EstadoEnvio estado) {
        return ResponseEntity.ok(envioService.actualizarEstado(id, estado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar envío")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        envioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
