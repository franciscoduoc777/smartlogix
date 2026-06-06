package cl.smartlogix.pedidos.dto.inventario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoInventarioDTO {

    private Long id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private Integer stock;
    private LocalDateTime fechaCreacion;
}
