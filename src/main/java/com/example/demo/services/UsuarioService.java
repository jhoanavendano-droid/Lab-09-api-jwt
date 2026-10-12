package com.example.demo.services;

import com.example.demo.dto.UsuarioResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.models.Usuario;
import com.example.demo.repository.UsuarioRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
            .orElseThrow(() ->
                new ResourceNotFoundException("Usuario", username)
            );
    }

    public UsuarioResponse obtenerPerfil(String username) {
        return UsuarioResponse.from(buscarPorUsername(username));
    }
}