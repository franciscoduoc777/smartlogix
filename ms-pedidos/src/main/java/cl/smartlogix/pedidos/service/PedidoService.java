package cl.smartlogix.pedidos.service;

import cl.smartlogix.pedidos.dto.DetallePedidoRequestDTO;
import cl.smartlogix.pedidos.dto.DetallePedidoResponseDTO;
import cl.smartlogix.pedidos.dto.PedidoRequestDTO;
import cl.smartlogix.pedidos.dto.PedidoResponseDTO;
import cl.smartlogix.pedidos.dto.inventario.ProductoInventarioDTO;
import cl.smartlogix.pedidos.exception.BusinessException;
import cl.smartlogix.pedidos.exception.ResourceNotFoundException;
import cl.smartlogix.pedidos.facade.InventarioFacade;
import cl.smartlogix.pedidos.factory.PedidoFactory;
import cl.smartlogix.pedidos.model.DetallePedido;
import cl.smartlogix.pedidos.model.EstadoPedido;
import cl.smartlogix.pedidos.model.Pedido;
import cl.smartlogix.pedidos.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final InventarioFacade inventarioFacade;

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listar() {
        return pedidoRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PedidoResponseDTO obtenerPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado: " + id));
        return toResponse(pedido);
    }

    @Transactional
    public PedidoResponseDTO crear(PedidoRequestDTO request) {
        if (request.getDetalles() == null || request.getDetalles().isEmpty()) {
            throw new BusinessException("No se permiten pedidos vacíos");
        }
        validarCantidades(request.getDetalles());
        BigDecimal total = calcularTotal(request.getDetalles());
        Pedido pedido = PedidoFactory.crearPedidoNuevo(request.getDetalles(), total);
        pedido.setEstado(EstadoPedido.VALIDADO);
        return toResponse(pedidoRepository.save(pedido));
    }

    @Transactional
    public PedidoResponseDTO aprobar(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado: " + id));
        if (pedido.getEstado() == EstadoPedido.APROBADO) {
            return toResponse(pedido);
        }
        if (pedido.getEstado() == EstadoPedido.RECHAZADO) {
            throw new BusinessException("No se puede aprobar un pedido rechazado");
        }
        for (DetallePedido detalle : pedido.getDetalles()) {
            if (!inventarioFacade.validarStock(detalle.getProductoId(), detalle.getCantidad())) {
                pedido.setEstado(EstadoPedido.RECHAZADO);
                pedidoRepository.save(pedido);
                throw new BusinessException("Stock insuficiente para producto id: " + detalle.getProductoId());
            }
        }
        for (DetallePedido detalle : pedido.getDetalles()) {
            inventarioFacade.descontarStock(detalle.getProductoId(), detalle.getCantidad());
        }
        pedido.setEstado(EstadoPedido.APROBADO);
        return toResponse(pedidoRepository.save(pedido));
    }

    @Transactional
    public PedidoResponseDTO rechazar(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado: " + id));
        pedido.setEstado(EstadoPedido.RECHAZADO);
        return toResponse(pedidoRepository.save(pedido));
    }

    @Transactional
    public PedidoResponseDTO marcarEnPreparacion(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado: " + id));
        if (pedido.getEstado() != EstadoPedido.APROBADO) {
            throw new BusinessException("Solo pedidos aprobados pueden pasar a preparación");
        }
        pedido.setEstado(EstadoPedido.EN_PREPARACION);
        return toResponse(pedidoRepository.save(pedido));
    }

    @Transactional(readOnly = true)
    public boolean estaAprobado(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado: " + id));
        return pedido.getEstado() == EstadoPedido.APROBADO
                || pedido.getEstado() == EstadoPedido.EN_PREPARACION;
    }

    private void validarCantidades(List<DetallePedidoRequestDTO> detalles) {
        for (DetallePedidoRequestDTO detalle : detalles) {
            if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                throw new BusinessException("La cantidad debe ser mayor a cero");
            }
            ProductoInventarioDTO producto = inventarioFacade.obtenerProducto(detalle.getProductoId());
            if (detalle.getPrecioUnitario() == null) {
                detalle.setPrecioUnitario(BigDecimal.valueOf(1000));
            }
            if (producto.getStock() < detalle.getCantidad()) {
                throw new BusinessException("Stock insuficiente al crear pedido para producto: " + producto.getNombre());
            }
        }
    }

    private BigDecimal calcularTotal(List<DetallePedidoRequestDTO> detalles) {
        return detalles.stream()
                .map(d -> {
                    BigDecimal precio = d.getPrecioUnitario() != null ? d.getPrecioUnitario() : BigDecimal.valueOf(1000);
                    return precio.multiply(BigDecimal.valueOf(d.getCantidad()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private PedidoResponseDTO toResponse(Pedido pedido) {
        List<DetallePedidoResponseDTO> detalles = pedido.getDetalles().stream()
                .map(d -> DetallePedidoResponseDTO.builder()
                        .id(d.getId())
                        .productoId(d.getProductoId())
                        .cantidad(d.getCantidad())
                        .precioUnitario(d.getPrecioUnitario())
                        .build())
                .toList();
        return PedidoResponseDTO.builder()
                .id(pedido.getId())
                .fecha(pedido.getFecha())
                .estado(pedido.getEstado())
                .total(pedido.getTotal())
                .detalles(detalles)
                .build();
    }
}
