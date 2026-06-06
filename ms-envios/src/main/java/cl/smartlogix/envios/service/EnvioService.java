package cl.smartlogix.envios.service;

import cl.smartlogix.envios.client.PedidosClient;
import cl.smartlogix.envios.dto.EnvioRequestDTO;
import cl.smartlogix.envios.dto.EnvioResponseDTO;
import cl.smartlogix.envios.exception.BusinessException;
import cl.smartlogix.envios.exception.ResourceNotFoundException;
import cl.smartlogix.envios.model.Envio;
import cl.smartlogix.envios.model.EstadoEnvio;
import cl.smartlogix.envios.repository.EnvioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnvioService {

    private final EnvioRepository envioRepository;
    private final PedidosClient pedidosClient;

    @Transactional(readOnly = true)
    public List<EnvioResponseDTO> listar() {
        return envioRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public EnvioResponseDTO obtenerPorId(Long id) {
        Envio envio = envioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Envío no encontrado: " + id));
        return toResponse(envio);
    }

    @Transactional
    public EnvioResponseDTO crear(EnvioRequestDTO request) {
        if (!pedidosClient.pedidoAprobado(request.getPedidoId())) {
            throw new BusinessException("Solo se puede generar envío para pedidos aprobados");
        }
        if (envioRepository.existsByPedidoId(request.getPedidoId())) {
            throw new BusinessException("Ya existe un envío para el pedido: " + request.getPedidoId());
        }
        Envio envio = new Envio();
        envio.setPedidoId(request.getPedidoId());
        envio.setDireccion(request.getDireccion());
        envio.setTransportista(request.getTransportista());
        envio.setFechaEstimada(request.getFechaEstimada());
        envio.setEstado(EstadoEnvio.PENDIENTE);
        return toResponse(envioRepository.save(envio));
    }

    @Transactional
    public EnvioResponseDTO actualizarEstado(Long id, EstadoEnvio nuevoEstado) {
        Envio envio = envioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Envío no encontrado: " + id));
        envio.setEstado(nuevoEstado);
        return toResponse(envioRepository.save(envio));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!envioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Envío no encontrado: " + id);
        }
        envioRepository.deleteById(id);
    }

    private EnvioResponseDTO toResponse(Envio envio) {
        return EnvioResponseDTO.builder()
                .id(envio.getId())
                .pedidoId(envio.getPedidoId())
                .direccion(envio.getDireccion())
                .transportista(envio.getTransportista())
                .fechaEstimada(envio.getFechaEstimada())
                .estado(envio.getEstado())
                .build();
    }
}
