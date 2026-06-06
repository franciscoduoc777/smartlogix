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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    // =========================
    // LISTAR
    // =========================
    @Test
    void listar_ok() {
        when(productoRepository.findAll()).thenReturn(List.of(producto));

        List<ProductoResponseDTO> result = productoService.listar();

        assertEquals(1, result.size());
        assertEquals("P001", result.get(0).getCodigo());
    }

    // =========================
    // OBTENER POR ID
    // =========================
    @Test
    void obtenerPorId_ok() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        ProductoResponseDTO result = productoService.obtenerPorId(1L);

        assertEquals("Caja", result.getNombre());
    }

    @Test
    void obtenerPorId_notFound() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productoService.obtenerPorId(99L));
    }

    // =========================
    // CREAR
    // =========================
    @Test
    void crear_ok() {
        when(productoRepository.existsByCodigo("P001")).thenReturn(false);
        when(productoRepository.save(any())).thenReturn(producto);

        ProductoResponseDTO result = productoService.crear(request);

        assertNotNull(result);
        assertEquals(10, result.getStock());
    }

    @Test
    void crear_codigoDuplicado() {
        when(productoRepository.existsByCodigo("P001")).thenReturn(true);

        assertThrows(BusinessException.class,
                () -> productoService.crear(request));

        verify(productoRepository, never()).save(any());
    }

    @Test
    void crear_stockNegativo() {
        ProductoRequestDTO bad = new ProductoRequestDTO("P002", "Caja", "Desc", -5);

        when(productoRepository.existsByCodigo("P002")).thenReturn(false);

        assertThrows(BusinessException.class,
                () -> productoService.crear(bad));
    }

    // =========================
    // ACTUALIZAR
    // =========================
    @Test
    void actualizar_ok() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.existsByCodigoAndIdNot(any(), any())).thenReturn(false);
        when(productoRepository.save(any())).thenReturn(producto);

        ProductoResponseDTO result = productoService.actualizar(1L, request);

        assertEquals("Caja", result.getNombre());
    }

    @Test
    void actualizar_notFound() {
        when(productoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productoService.actualizar(1L, request));
    }

    // =========================
    // ELIMINAR
    // =========================
    @Test
    void eliminar_ok() {
        when(productoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productoRepository).deleteById(1L);

        assertDoesNotThrow(() -> productoService.eliminar(1L));

        verify(productoRepository).deleteById(1L);
    }

    @Test
    void eliminar_notFound() {
        when(productoRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> productoService.eliminar(1L));
    }

    // =========================
    // DESCONTAR STOCK
    // =========================
    @Test
    void descontarStock_ok() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any())).thenReturn(producto);

        DescontarStockDTO dto = new DescontarStockDTO(5);

        ProductoResponseDTO result = productoService.descontarStock(1L, dto);

        assertNotNull(result);
    }

    @Test
    void descontarStock_insuficiente() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        DescontarStockDTO dto = new DescontarStockDTO(50);

        assertThrows(BusinessException.class,
                () -> productoService.descontarStock(1L, dto));
    }

    // =========================
    // STOCK SUFICIENTE
    // =========================
    @Test
    void tieneStockSuficiente_true() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        assertTrue(productoService.tieneStockSuficiente(1L, 5));
    }

    @Test
    void tieneStockSuficiente_false() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        assertFalse(productoService.tieneStockSuficiente(1L, 50));
    }
}