package cl.smartlogix.inventario.service;

import cl.smartlogix.inventario.dto.DescontarStockDTO;
import cl.smartlogix.inventario.dto.ProductoRequestDTO;
import cl.smartlogix.inventario.dto.ProductoResponseDTO;
import cl.smartlogix.inventario.exception.BusinessException;
import cl.smartlogix.inventario.exception.ResourceNotFoundException;
import cl.smartlogix.inventario.model.Producto;
import cl.smartlogix.inventario.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto;
    private ProductoRequestDTO request;

    @BeforeEach
    void setUp() {
        producto = new Producto(1L, "P001", "Caja", "Descripción", 10, LocalDateTime.now());
        request = new ProductoRequestDTO("P001", "Caja", "Descripción", 10);
    }

    @Test
    void listar_debeRetornarProductos() {
        when(productoRepository.findAll()).thenReturn(List.of(producto));
        List<ProductoResponseDTO> resultado = productoService.listar();
        assertEquals(1, resultado.size());
        assertEquals("P001", resultado.get(0).getCodigo());
    }

    @Test
    void obtenerPorId_cuandoExiste_debeRetornarProducto() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        ProductoResponseDTO resultado = productoService.obtenerPorId(1L);
        assertEquals("Caja", resultado.getNombre());
    }

    @Test
    void obtenerPorId_cuandoNoExiste_debeLanzarExcepcion() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productoService.obtenerPorId(99L));
    }

    @Test
    void crear_conCodigoDuplicado_debeLanzarExcepcion() {
        when(productoRepository.existsByCodigo("P001")).thenReturn(true);
        assertThrows(BusinessException.class, () -> productoService.crear(request));
        verify(productoRepository, never()).save(any());
    }

    @Test
    void crear_valido_debeGuardarProducto() {
        when(productoRepository.existsByCodigo("P001")).thenReturn(false);
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);
        ProductoResponseDTO resultado = productoService.crear(request);
        assertNotNull(resultado);
        assertEquals(10, resultado.getStock());
    }

    @Test
    void descontarStock_sinStockSuficiente_debeLanzarExcepcion() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        DescontarStockDTO dto = new DescontarStockDTO(20);
        assertThrows(BusinessException.class, () -> productoService.descontarStock(1L, dto));
    }

    @Test
    void tieneStockSuficiente_debeRetornarTrue() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        assertTrue(productoService.tieneStockSuficiente(1L, 5));
    }

    @Test
    void eliminar_cuandoNoExiste_debeLanzarExcepcion() {
        when(productoRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> productoService.eliminar(1L));
    }
}
