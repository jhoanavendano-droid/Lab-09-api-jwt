package com.example.demo.services;

import com.example.demo.dto.ProductoRequest;
import com.example.demo.dto.ProductoResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.models.CategoriaProducto;
import com.example.demo.models.Producto;
import com.example.demo.repository.ProductoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public ProductoResponse crear(ProductoRequest request) {
        Producto producto = new Producto();

        producto.setNombre(request.nombre());
        producto.setDescripcion(request.descripcion());
        producto.setCategoria(request.categoria());
        producto.setPrecio(request.precio());

        return ProductoResponse.from(
            productoRepository.save(producto)
        );
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> listar(CategoriaProducto categoria) {

        List<Producto> productos = categoria == null
            ? productoRepository.findAll()
            : productoRepository.findByCategoria(categoria);

        return productos.stream()
            .map(ProductoResponse::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("Producto", id)
            );
    }

    @Transactional(readOnly = true)
    public ProductoResponse obtenerPorId(Long id) {
        return ProductoResponse.from(buscarPorId(id));
    }
}