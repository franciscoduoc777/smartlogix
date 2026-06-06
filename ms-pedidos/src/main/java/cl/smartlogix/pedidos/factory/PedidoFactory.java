package cl.smartlogix.pedidos.factory;

import cl.smartlogix.pedidos.dto.DetallePedidoRequestDTO;
import cl.smartlogix.pedidos.model.DetallePedido;
import cl.smartlogix.pedidos.model.EstadoPedido;
import cl.smartlogix.pedidos.model.Pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Patrón Factory Method: centraliza la creación de instancias de {@link Pedido}
 * según el tipo de operación, evitando lógica de construcción dispersa en el servicio.
 */
public final class PedidoFactory {

    private PedidoFactory() {
    }

    public static Pedido crearPedidoNuevo(List<DetallePedidoRequestDTO> detallesRequest,
                                          BigDecimal total) {
        Pedido pedido = new Pedido();
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.CREADO);
        pedido.setTotal(total);
        for (DetallePedidoRequestDTO detalleDto : detallesRequest) {
            DetallePedido detalle = new DetallePedido();
            detalle.setProductoId(detalleDto.getProductoId());
            detalle.setCantidad(detalleDto.getCantidad());
            detalle.setPrecioUnitario(detalleDto.getPrecioUnitario() != null
                    ? detalleDto.getPrecioUnitario()
                    : BigDecimal.ZERO);
            detalle.setPedido(pedido);
            pedido.getDetalles().add(detalle);
        }
        return pedido;
    }

    public static Pedido crearPedidoRechazado(Pedido original, String motivo) {
        original.setEstado(EstadoPedido.RECHAZADO);
        return original;
    }
}
