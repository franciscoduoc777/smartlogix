package cl.smartlogix.inventario.repository;

import cl.smartlogix.inventario.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Patrón Repository: abstrae el acceso a datos y desacopla la capa de servicio
 * de los detalles de persistencia JPA.
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    boolean existsByCodigo(String codigo);

    Optional<Producto> findByCodigo(String codigo);

    boolean existsByCodigoAndIdNot(String codigo, Long id);
}
