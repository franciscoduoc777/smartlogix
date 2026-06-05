package cl.smartlogix.bff.facade;

import cl.smartlogix.bff.dto.DashboardResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Patrón Facade: unifica llamadas a inventario, pedidos y envíos para el dashboard,
 * ocultando la complejidad de múltiples microservicios al frontend.
 */
@Component
@RequiredArgsConstructor
public class LogisticaFacade {

    private final RestTemplate restTemplate;

    @Value("${smartlogix.inventario.url}")
    private String inventarioUrl;

    @Value("${smartlogix.pedidos.url}")
    private String pedidosUrl;

    @Value("${smartlogix.envios.url}")
    private String enviosUrl;

    public DashboardResponseDTO obtenerDashboard() {
        List<Map<String, Object>> productos = obtenerProductos();
        Map<String, Object> pedido = obtenerUltimoPedido();
        Map<String, Object> envio = obtenerUltimoEnvio();

        return DashboardResponseDTO.builder()
                .productos(productos)
                .pedido(pedido)
                .envio(envio)
                .build();
    }

    private List<Map<String, Object>> obtenerProductos() {
        try {
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    inventarioUrl + "/api/productos",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {});
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }

    private Map<String, Object> obtenerUltimoPedido() {
        try {
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    pedidosUrl + "/api/pedidos",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {});
            List<Map<String, Object>> pedidos = response.getBody();
            if (pedidos == null || pedidos.isEmpty()) {
                return Collections.emptyMap();
            }
            return pedidos.get(pedidos.size() - 1);
        } catch (Exception ex) {
            return Collections.emptyMap();
        }
    }

    private Map<String, Object> obtenerUltimoEnvio() {
        try {
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    enviosUrl + "/api/envios",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {});
            List<Map<String, Object>> envios = response.getBody();
            if (envios == null || envios.isEmpty()) {
                return Collections.emptyMap();
            }
            return envios.get(envios.size() - 1);
        } catch (Exception ex) {
            return Collections.emptyMap();
        }
    }
}
