package com.example.demo.controllers;

import com.example.demo.dto.CalificacionRequest;
import com.example.demo.dto.CalificacionResponse;
import com.example.demo.services.CalificacionService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calificaciones")
public class CalificacionController {

    private final CalificacionService calificacionService;

    public CalificacionController(
            CalificacionService calificacionService) {

        this.calificacionService = calificacionService;
    }

    @PostMapping
    public ResponseEntity<CalificacionResponse> crear(
            @Valid @RequestBody CalificacionRequest request,
            Authentication authentication) {

        CalificacionResponse creada = calificacionService.crear(
            authentication.getName(),
            request
        );

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(creada);
    }

    @GetMapping("/mias")
    public List<CalificacionResponse> listarMias(
            Authentication authentication) {

        return calificacionService.listarMias(
            authentication.getName()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id,
            Authentication authentication) {

        boolean esAdmin = authentication.getAuthorities()
            .stream()
            .anyMatch(permiso ->
                permiso.getAuthority().equals("ROLE_ADMIN")
            );

        calificacionService.eliminar(
            id,
            authentication.getName(),
            esAdmin
        );

        return ResponseEntity.noContent().build();
    }
}