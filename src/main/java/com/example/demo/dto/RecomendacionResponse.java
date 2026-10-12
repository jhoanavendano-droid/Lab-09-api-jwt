package com.example.demo.dto;

import com.example.demo.models.CategoriaProducto;

import java.math.BigDecimal;

public record RecomendacionResponse(
    Long id,
    String nombre,
    CategoriaProducto categoria,
    BigDecimal precio,
    Double promedio,
    Long totalCalificaciones
) {}