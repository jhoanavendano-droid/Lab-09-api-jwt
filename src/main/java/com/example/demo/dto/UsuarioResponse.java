package com.example.demo.dto;

import com.example.demo.models.Rol;
import com.example.demo.models.Usuario;

public record UsuarioResponse(
    Long id,
    String username,
    String nombre,
    Rol rol
) {

    public static UsuarioResponse from(Usuario usuario) {
        return new UsuarioResponse(
            usuario.getId(),
            usuario.getUsername(),
            usuario.getNombre(),
            usuario.getRol()
        );
    }
}