package cl.smartlogix.bff.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProxyService {

    private final RestTemplate restTemplate;

    @Value("${smartlogix.inventario.url}")
    private String inventarioUrl;

    @Value("${smartlogix.pedidos.url}")
    private String pedidosUrl;

    @Value("${smartlogix.envios.url}")
    private String enviosUrl;

    public List<Map<String, Object>> listarProductos() {
        return exchangeList(inventarioUrl + "/api/productos");
    }

    public Map<String, Object> obtenerProducto(Long id) {
        return restTemplate.getForObject(inventarioUrl + "/api/productos/" + id, Map.class);
    }

    public Map<String, Object> crearProducto(Map<String, Object> body) {
        return restTemplate.postForObject(inventarioUrl + "/api/productos", body, Map.class);
    }

    public Map<String, Object> actualizarProducto(Long id, Map<String, Object> body) {
        restTemplate.put(inventarioUrl + "/api/productos/" + id, body);
        return obtenerProducto(id);
    }

    public void eliminarProducto(Long id) {
        restTemplate.delete(inventarioUrl + "/api/productos/" + id);
    }

    public List<Map<String, Object>> listarPedidos() {
        return exchangeList(pedidosUrl + "/api/pedidos");
    }

    public Map<String, Object> crearPedido(Map<String, Object> body) {
        return restTemplate.postForObject(pedidosUrl + "/api/pedidos", body, Map.class);
    }

    public Map<String, Object> aprobarPedido(Long id) {
        return restTemplate.exchange(
                pedidosUrl + "/api/pedidos/" + id + "/aprobar",
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                Map.class).getBody();
    }

    public Map<String, Object> rechazarPedido(Long id) {
        return restTemplate.exchange(
                pedidosUrl + "/api/pedidos/" + id + "/rechazar",
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                Map.class).getBody();
    }

    public List<Map<String, Object>> listarEnvios() {
        return exchangeList(enviosUrl + "/api/envios");
    }

    public Map<String, Object> crearEnvio(Map<String, Object> body) {
        return restTemplate.postForObject(enviosUrl + "/api/envios", body, Map.class);
    }

    public Map<String, Object> actualizarEstadoEnvio(Long id, String estado) {
        return restTemplate.exchange(
                enviosUrl + "/api/envios/" + id + "/estado?estado=" + estado,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                Map.class).getBody();
    }

    private List<Map<String, Object>> exchangeList(String url) {
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {});
        return response.getBody();
    }
}
