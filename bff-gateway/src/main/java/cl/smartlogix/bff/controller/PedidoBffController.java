package cl.smartlogix.bff.controller;

import cl.smartlogix.bff.service.ProxyService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Pedidos BFF")
public class PedidoBffController {

    private final ProxyService proxyService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listar() {
        return ResponseEntity.ok(proxyService.listarPedidos());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@RequestBody Map<String, Object> body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(proxyService.crearPedido(body));
    }

    @PutMapping("/{id}/aprobar")
    public ResponseEntity<Map<String, Object>> aprobar(@PathVariable Long id) {
        return ResponseEntity.ok(proxyService.aprobarPedido(id));
    }

    @PutMapping("/{id}/rechazar")
    public ResponseEntity<Map<String, Object>> rechazar(@PathVariable Long id) {
        return ResponseEntity.ok(proxyService.rechazarPedido(id));
    }
}
