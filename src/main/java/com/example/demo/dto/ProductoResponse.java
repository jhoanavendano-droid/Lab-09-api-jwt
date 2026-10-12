package com.example.demo.dto;

import com.example.demo.models.CategoriaProducto;
import com.example.demo.models.Producto;

import java.math.BigDecimal;

public record ProductoResponse(
    Long id,
    String nombre,
    String descripcion,
    CategoriaProducto categoria,
    BigDecimal precio
) {

    public static ProductoResponse from(Producto producto) {
        return new ProductoResponse(
            producto.getId(),
            producto.getNombre(),
            producto.getDescripcion(),
            producto.getCategoria(),
            producto.getPrecio()
        );
    }
}