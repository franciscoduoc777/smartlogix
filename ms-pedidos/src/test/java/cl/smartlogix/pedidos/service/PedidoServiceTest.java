package cl.smartlogix.pedidos.service;

import cl.smartlogix.pedidos.dto.DetallePedidoRequestDTO;
import cl.smartlogix.pedidos.dto.PedidoRequestDTO;
import cl.smartlogix.pedidos.dto.PedidoResponseDTO;
import cl.smartlogix.pedidos.dto.inventario.ProductoInventarioDTO;
import cl.smartlogix.pedidos.exception.BusinessException;
import cl.smartlogix.pedidos.exception.ResourceNotFoundException;
import cl.smartlogix.pedidos.facade.InventarioFacade;
import cl.smartlogix.pedidos.model.DetallePedido;
import cl.smartlogix.pedidos.model.EstadoPedido;
import cl.smartlogix.pedidos.model.Pedido;
import cl.smartlogix.pedidos.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private InventarioFacade inventarioFacade;

    @InjectMocks
    private PedidoService pedidoService;

    private Pedido pedido;
    private DetallePedido detalle;

    @BeforeEach
    void setUp() {
        pedido = new Pedido();
        pedido.setId(1L);
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.VALIDADO);
        pedido.setTotal(BigDecimal.valueOf(5000));

        detalle = new DetallePedido();
        detalle.setId(1L);
        detalle.setProductoId(10L);
        detalle.setCantidad(2);
        detalle.setPrecioUnitario(BigDecimal.valueOf(2500));
        detalle.setPedido(pedido);
        pedido.setDetalles(List.of(detalle));
    }

    @Test
    void crear_pedidoVacio_debeLanzarExcepcion() {
        PedidoRequestDTO request = new PedidoRequestDTO(List.of());
        assertThrows(BusinessException.class, () -> pedidoService.crear(request));
    }

    @Test
    void aprobar_sinStock_debeRechazarPedido() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(inventarioFacade.validarStock(10L, 2)).thenReturn(false);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        assertThrows(BusinessException.class, () -> pedidoService.aprobar(1L));
        verify(pedidoRepository).save(any(Pedido.class));
    }

    @Test
    void aprobar_conStock_debeAprobarYDescontar() {
        pedido.setEstado(EstadoPedido.VALIDADO);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(inventarioFacade.validarStock(10L, 2)).thenReturn(true);
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

        PedidoResponseDTO resultado = pedidoService.aprobar(1L);
        assertEquals(EstadoPedido.APROBADO, resultado.getEstado());
        verify(inventarioFacade).descontarStock(10L, 2);
    }

    @Test
    void obtenerPorId_noExiste_debeLanzarExcepcion() {
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> pedidoService.obtenerPorId(99L));
    }

    @Test
    void crear_valido_debeGuardarPedido() {
        DetallePedidoRequestDTO detalleDto = new DetallePedidoRequestDTO(10L, 2, BigDecimal.valueOf(2500));
        PedidoRequestDTO request = new PedidoRequestDTO(List.of(detalleDto));
        ProductoInventarioDTO producto = new ProductoInventarioDTO(10L, "P10", "Prod", "Desc", 100, LocalDateTime.now());

        when(inventarioFacade.obtenerProducto(10L)).thenReturn(producto);
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> {
            Pedido p = inv.getArgument(0);
            p.setId(1L);
            return p;
        });

        PedidoResponseDTO resultado = pedidoService.crear(request);
        assertEquals(EstadoPedido.VALIDADO, resultado.getEstado());
    }

    @Test
    void estaAprobado_cuandoAprobado_debeRetornarTrue() {
        pedido.setEstado(EstadoPedido.APROBADO);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        assertEquals(true, pedidoService.estaAprobado(1L));
    }
}
