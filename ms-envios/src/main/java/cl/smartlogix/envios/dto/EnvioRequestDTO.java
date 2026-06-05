package cl.smartlogix.envios.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnvioRequestDTO {

    @NotNull(message = "El pedidoId es obligatorio")
    private Long pedidoId;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotBlank(message = "El transportista es obligatorio")
    private String transportista;

    @NotNull(message = "La fecha estimada es obligatoria")
    @FutureOrPresent(message = "La fecha estimada debe ser hoy o futura")
    private LocalDate fechaEstimada;
}
