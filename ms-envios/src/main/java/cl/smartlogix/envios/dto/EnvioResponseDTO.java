package cl.smartlogix.envios.dto;

import cl.smartlogix.envios.model.EstadoEnvio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnvioResponseDTO {

    private Long id;
    private Long pedidoId;
    private String direccion;
    private String transportista;
    private LocalDate fechaEstimada;
    private EstadoEnvio estado;
}
