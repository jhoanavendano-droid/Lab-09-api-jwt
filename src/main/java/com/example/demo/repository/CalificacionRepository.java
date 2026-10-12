package com.example.demo.repository;

import com.example.demo.dto.RecomendacionResponse;
import com.example.demo.models.Calificacion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CalificacionRepository
        extends JpaRepository<Calificacion, Long> {

    boolean existsByUsuarioIdAndProductoId(
        Long usuarioId,
        Long productoId
    );

    List<Calificacion> findByUsuarioIdOrderByFechaDesc(
        Long usuarioId
    );

    @Query("""
        SELECT new com.example.demo.dto.RecomendacionResponse(
            p.id,
            p.nombre,
            p.categoria,
            p.precio,
            AVG(c.puntuacion),
            COUNT(c)
        )
        FROM Calificacion c
        JOIN c.producto p
        WHERE NOT EXISTS (
            SELECT c2.id
            FROM Calificacion c2
            WHERE c2.producto.id = p.id
              AND c2.usuario.id = :usuarioId
        )
        GROUP BY p.id, p.nombre, p.categoria, p.precio
        HAVING AVG(c.puntuacion) >= 4.0
        ORDER BY AVG(c.puntuacion) DESC, COUNT(c) DESC, p.id ASC
        """)
    List<RecomendacionResponse> recomendarParaUsuario(
        @Param("usuarioId") Long usuarioId
    );
}