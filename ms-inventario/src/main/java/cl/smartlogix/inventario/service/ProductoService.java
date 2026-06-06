package cl.smartlogix.inventario.service;

import cl.smartlogix.inventario.builder.ProductoBuilder;
import cl.smartlogix.inventario.dto.DescontarStockDTO;
import cl.smartlogix.inventario.dto.ProductoRequestDTO;
import cl.smartlogix.inventario.dto.ProductoResponseDTO;
import cl.smartlogix.inventario.exception.BusinessException;
import cl.smartlogix.inventario.exception.ResourceNotFoundException;
import cl.smartlogix.inventario.model.Producto;
import cl.smartlogix.inventario.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listar() {
        return productoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductoResponseDTO obtenerPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        return toResponse(producto);
    }

    @Transactional
    public ProductoResponseDTO crear(ProductoRequestDTO request) {
        validarCodigoDuplicado(request.getCodigo(), null);
        validarStockNoNegativo(request.getStock());
        Producto producto = ProductoBuilder.desdeRequest(request).build();
        return toResponse(productoRepository.save(producto));
    }

    @Transactional
    public ProductoResponseDTO actualizar(Long id, ProductoRequestDTO request) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        validarCodigoDuplicado(request.getCodigo(), id);
        validarStockNoNegativo(request.getStock());
        producto.setCodigo(request.getCodigo());
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setStock(request.getStock());
        return toResponse(productoRepository.save(producto));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado con id: " + id);
        }
        productoRepository.deleteById(id);
    }

    @Transactional
    public ProductoResponseDTO descontarStock(Long id, DescontarStockDTO dto) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        if (producto.getStock() < dto.getCantidad()) {
            throw new BusinessException("Stock insuficiente para el producto: " + producto.getNombre());
        }
        producto.setStock(producto.getStock() - dto.getCantidad());
        return toResponse(productoRepository.save(producto));
    }

    @Transactional(readOnly = true)
    public boolean tieneStockSuficiente(Long id, Integer cantidad) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        return producto.getStock() >= cantidad;
    }

    private void validarCodigoDuplicado(String codigo, Long idExcluir) {
        boolean duplicado = idExcluir == null
                ? productoRepository.existsByCodigo(codigo)
                : productoRepository.existsByCodigoAndIdNot(codigo, idExcluir);
        if (duplicado) {
            throw new BusinessException("Ya existe un producto con el código: " + codigo);
        }
    }

    private void validarStockNoNegativo(Integer stock) {
        if (stock == null || stock < 0) {
            throw new BusinessException("El stock no puede ser negativo");
        }
    }

    private ProductoResponseDTO toResponse(Producto producto) {
        return ProductoResponseDTO.builder()
                .id(producto.getId())
                .codigo(producto.getCodigo())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .stock(producto.getStock())
                .fechaCreacion(producto.getFechaCreacion())
                .build();
    }
}
