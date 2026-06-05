package cl.smartlogix.pedidos.dto;

import cl.smartlogix.pedidos.model.EstadoPedido;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoResponseDTO {

    private Long id;
    private LocalDateTime fecha;
    private EstadoPedido estado;
    private BigDecimal total;
    private List<DetallePedidoResponseDTO> detalles;
}
