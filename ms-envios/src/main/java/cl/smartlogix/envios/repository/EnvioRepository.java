package cl.smartlogix.envios.repository;

import cl.smartlogix.envios.model.Envio;
import cl.smartlogix.envios.model.EstadoEnvio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Patrón Repository: abstrae el acceso a datos de envíos.
 */
@Repository
public interface EnvioRepository extends JpaRepository<Envio, Long> {

    List<Envio> findByPedidoId(Long pedidoId);

    Optional<Envio> findFirstByEstadoOrderByIdDesc(EstadoEnvio estado);

    boolean existsByPedidoId(Long pedidoId);
}
