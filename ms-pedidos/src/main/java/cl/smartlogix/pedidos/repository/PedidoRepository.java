package cl.smartlogix.pedidos.repository;

import cl.smartlogix.pedidos.model.EstadoPedido;
import cl.smartlogix.pedidos.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Patrón Repository: abstrae persistencia de pedidos.
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByEstado(EstadoPedido estado);

    Optional<Pedido> findFirstByEstadoOrderByFechaDesc(EstadoPedido estado);
}
