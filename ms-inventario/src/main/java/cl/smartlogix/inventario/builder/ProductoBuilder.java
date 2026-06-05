package cl.smartlogix.inventario.builder;

import cl.smartlogix.inventario.dto.ProductoRequestDTO;
import cl.smartlogix.inventario.model.Producto;

import java.time.LocalDateTime;

/**
 * Patrón Builder: encapsula la construcción paso a paso de {@link Producto}.
 * Resuelve la complejidad de crear objetos con muchos atributos y garantiza
 * valores por defecto coherentes (por ejemplo fechaCreacion).
 */
public class ProductoBuilder {

    private String codigo;
    private String nombre;
    private String descripcion;
    private Integer stock;
    private LocalDateTime fechaCreacion;

    public ProductoBuilder codigo(String codigo) {
        this.codigo = codigo;
        return this;
    }

    public ProductoBuilder nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public ProductoBuilder descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public ProductoBuilder stock(Integer stock) {
        this.stock = stock;
        return this;
    }

    public ProductoBuilder fechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
        return this;
    }

    public static ProductoBuilder desdeRequest(ProductoRequestDTO request) {
        return new ProductoBuilder()
                .codigo(request.getCodigo())
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .stock(request.getStock())
                .fechaCreacion(LocalDateTime.now());
    }

    public Producto build() {
        Producto producto = new Producto();
        producto.setCodigo(codigo);
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setStock(stock);
        producto.setFechaCreacion(fechaCreacion != null ? fechaCreacion : LocalDateTime.now());
        return producto;
    }
}
