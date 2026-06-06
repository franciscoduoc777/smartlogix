package cl.smartlogix.inventario.controller;

import cl.smartlogix.inventario.dto.DescontarStockDTO;
import cl.smartlogix.inventario.dto.ProductoRequestDTO;
import cl.smartlogix.inventario.dto.ProductoResponseDTO;
import cl.smartlogix.inventario.service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    // =========================
    // LISTAR
    // =========================
    @Test
    void listar_ok() throws Exception {
        Mockito.when(productoService.listar()).thenReturn(List.of());

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk());
    }

    // =========================
    // OBTENER POR ID
    // =========================
    @Test
    void obtener_ok() throws Exception {
        Mockito.when(productoService.obtenerPorId(anyLong()))
                .thenReturn(new ProductoResponseDTO());

        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk());
    }

    // =========================
    // CREAR
    // =========================
    @Test
    void crear_ok() throws Exception {
        ProductoRequestDTO request = new ProductoRequestDTO("P001", "Caja", "Desc", 10);

        Mockito.when(productoService.crear(any()))
                .thenReturn(new ProductoResponseDTO());

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    // =========================
    // ACTUALIZAR
    // =========================
    @Test
    void actualizar_ok() throws Exception {
        ProductoRequestDTO request = new ProductoRequestDTO("P001", "Caja", "Desc", 10);

        Mockito.when(productoService.actualizar(anyLong(), any()))
                .thenReturn(new ProductoResponseDTO());

        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    // =========================
    // ELIMINAR
    // =========================
    @Test
    void eliminar_ok() throws Exception {
        Mockito.doNothing().when(productoService).eliminar(anyLong());

        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isNoContent());
    }

    // =========================
    // DESCONTAR STOCK
    // =========================
    @Test
    void descontarStock_ok() throws Exception {
        DescontarStockDTO dto = new DescontarStockDTO(5);

        Mockito.when(productoService.descontarStock(anyLong(), any()))
                .thenReturn(new ProductoResponseDTO());

        mockMvc.perform(post("/api/productos/1/descontar-stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    // =========================
    // STOCK SUFICIENTE
    // =========================
    @Test
    void stockSuficiente_ok() throws Exception {
        Mockito.when(productoService.tieneStockSuficiente(anyLong(), any()))
                .thenReturn(true);

        mockMvc.perform(get("/api/productos/1/stock-suficiente/5"))
                .andExpect(status().isOk());
    }
}