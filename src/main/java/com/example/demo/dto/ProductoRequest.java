package com.example.demo.dto;

import com.example.demo.models.CategoriaProducto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductoRequest(

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "Máximo 100 caracteres")
    String nombre,

    @Size(max = 255, message = "Máximo 255 caracteres")
    String descripcion,

    @NotNull(message = "La categoría es obligatoria")
    CategoriaProducto categoria,

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor que cero")
    BigDecimal precio

) {}