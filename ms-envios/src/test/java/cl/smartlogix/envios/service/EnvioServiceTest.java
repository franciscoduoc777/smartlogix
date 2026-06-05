package cl.smartlogix.envios.service;

import cl.smartlogix.envios.client.PedidosClient;
import cl.smartlogix.envios.dto.EnvioRequestDTO;
import cl.smartlogix.envios.dto.EnvioResponseDTO;
import cl.smartlogix.envios.exception.BusinessException;
import cl.smartlogix.envios.exception.ResourceNotFoundException;
import cl.smartlogix.envios.model.Envio;
import cl.smartlogix.envios.model.EstadoEnvio;
import cl.smartlogix.envios.repository.EnvioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnvioServiceTest {

    @Mock
    private EnvioRepository envioRepository;

    @Mock
    private PedidosClient pedidosClient;

    @InjectMocks
    private EnvioService envioService;

    private EnvioRequestDTO request;
    private Envio envio;

    @BeforeEach
    void setUp() {
        request = new EnvioRequestDTO(1L, "Av. Principal 100", "Chilexpress", LocalDate.now().plusDays(3));
        envio = new Envio(1L, 1L, "Av. Principal 100", "Chilexpress", LocalDate.now().plusDays(3), EstadoEnvio.PENDIENTE);
    }

    @Test
    void crear_pedidoNoAprobado_debeLanzarExcepcion() {
        when(pedidosClient.pedidoAprobado(1L)).thenReturn(false);
        assertThrows(BusinessException.class, () -> envioService.crear(request));
        verify(envioRepository, never()).save(any());
    }

    @Test
    void crear_pedidoAprobado_debeCrearEnvio() {
        when(pedidosClient.pedidoAprobado(1L)).thenReturn(true);
        when(envioRepository.existsByPedidoId(1L)).thenReturn(false);
        when(envioRepository.save(any(Envio.class))).thenReturn(envio);

        EnvioResponseDTO resultado = envioService.crear(request);
        assertEquals(EstadoEnvio.PENDIENTE, resultado.getEstado());
        assertEquals(1L, resultado.getPedidoId());
    }

    @Test
    void crear_envioDuplicado_debeLanzarExcepcion() {
        when(pedidosClient.pedidoAprobado(1L)).thenReturn(true);
        when(envioRepository.existsByPedidoId(1L)).thenReturn(true);
        assertThrows(BusinessException.class, () -> envioService.crear(request));
    }

    @Test
    void obtenerPorId_noExiste_debeLanzarExcepcion() {
        when(envioRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> envioService.obtenerPorId(99L));
    }

    @Test
    void actualizarEstado_debeActualizar() {
        when(envioRepository.findById(1L)).thenReturn(Optional.of(envio));
        envio.setEstado(EstadoEnvio.EN_TRANSITO);
        when(envioRepository.save(envio)).thenReturn(envio);

        EnvioResponseDTO resultado = envioService.actualizarEstado(1L, EstadoEnvio.EN_TRANSITO);
        assertEquals(EstadoEnvio.EN_TRANSITO, resultado.getEstado());
    }
}
