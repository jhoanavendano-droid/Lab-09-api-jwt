package com.example.demo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CalificacionRequest(

    @NotNull(message = "El producto es obligatorio")
    @Positive(message = "El identificador del producto debe ser positivo")
    Long productoId,

    @NotNull(message = "La puntuación es obligatoria")
    @Min(value = 1, message = "La puntuación mínima es 1")
    @Max(value = 5, message = "La puntuación máxima es 5")
    Integer puntuacion,

    @Size(max = 300, message = "Máximo 300 caracteres")
    String comentario

) {}