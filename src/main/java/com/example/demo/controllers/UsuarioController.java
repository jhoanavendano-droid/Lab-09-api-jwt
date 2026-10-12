package com.example.demo.controllers;

import com.example.demo.dto.UsuarioResponse;
import com.example.demo.services.UsuarioService;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/me")
    public UsuarioResponse obtenerPerfil(Authentication authentication) {
        return usuarioService.obtenerPerfil(
            authentication.getName()
        );
    }
}