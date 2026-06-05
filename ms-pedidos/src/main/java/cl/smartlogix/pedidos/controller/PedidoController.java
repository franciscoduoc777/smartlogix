package cl.smartlogix.pedidos.controller;

import cl.smartlogix.pedidos.dto.PedidoRequestDTO;
import cl.smartlogix.pedidos.dto.PedidoResponseDTO;
import cl.smartlogix.pedidos.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "API de gestión de pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping
    @Operation(summary = "Listar pedidos")
    public ResponseEntity<List<PedidoResponseDTO>> listar() {
        return ResponseEntity.ok(pedidoService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener pedido por id")
    public ResponseEntity<PedidoResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear pedido")
    public ResponseEntity<PedidoResponseDTO> crear(@Valid @RequestBody PedidoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.crear(request));
    }

    @PutMapping("/{id}/aprobar")
    @Operation(summary = "Aprobar pedido y descontar stock")
    public ResponseEntity<PedidoResponseDTO> aprobar(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.aprobar(id));
    }

    @PutMapping("/{id}/rechazar")
    @Operation(summary = "Rechazar pedido")
    public ResponseEntity<PedidoResponseDTO> rechazar(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.rechazar(id));
    }

    @PutMapping("/{id}/preparacion")
    @Operation(summary = "Marcar pedido en preparación")
    public ResponseEntity<PedidoResponseDTO> preparacion(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.marcarEnPreparacion(id));
    }

    @GetMapping("/{id}/aprobado")
    @Operation(summary = "Verificar si el pedido está aprobado")
    public ResponseEntity<Map<String, Boolean>> estaAprobado(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of("aprobado", pedidoService.estaAprobado(id)));
    }
}
