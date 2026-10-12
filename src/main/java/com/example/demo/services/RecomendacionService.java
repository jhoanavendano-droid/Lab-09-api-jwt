package com.example.demo.services;

import com.example.demo.dto.RecomendacionResponse;
import com.example.demo.models.Usuario;
import com.example.demo.repository.CalificacionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RecomendacionService {

    private final CalificacionRepository calificacionRepository;
    private final UsuarioService usuarioService;

    public RecomendacionService(
            CalificacionRepository calificacionRepository,
            UsuarioService usuarioService) {

        this.calificacionRepository = calificacionRepository;
        this.usuarioService = usuarioService;
    }

    public List<RecomendacionResponse> recomendar(String username) {

        Usuario usuario = usuarioService.buscarPorUsername(username);

        return calificacionRepository.recomendarParaUsuario(
            usuario.getId()
        );
    }
}