package cl.smartlogix.envios.client;

import cl.smartlogix.envios.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PedidosClient {

    private final RestTemplate restTemplate;

    @Value("${smartlogix.pedidos.base-url}")
    private String pedidosBaseUrl;

    public boolean pedidoAprobado(Long pedidoId) {
        try {
            String url = pedidosBaseUrl + "/api/pedidos/" + pedidoId + "/aprobado";
            Map<?, ?> response = restTemplate.getForObject(url, Map.class);
            if (response == null || !response.containsKey("aprobado")) {
                return false;
            }
            return Boolean.TRUE.equals(response.get("aprobado"));
        } catch (RestClientException ex) {
            throw new BusinessException("Error al validar pedido: " + ex.getMessage());
        }
    }
}
