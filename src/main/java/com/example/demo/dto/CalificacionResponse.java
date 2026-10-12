package com.example.demo.dto;

import com.example.demo.models.Calificacion;

import java.time.LocalDate;

public record CalificacionResponse(
    Long id,
    String usuario,
    Long productoId,
    String producto,
    Integer puntuacion,
    String comentario,
    LocalDate fecha
) {

    public static CalificacionResponse from(Calificacion calificacion) {
        return new CalificacionResponse(
            calificacion.getId(),
            calificacion.getUsuario().getUsername(),
            calificacion.getProducto().getId(),
            calificacion.getProducto().getNombre(),
            calificacion.getPuntuacion(),
            calificacion.getComentario(),
            calificacion.getFecha()
        );
    }
}