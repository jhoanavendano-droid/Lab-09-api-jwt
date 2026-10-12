package com.example.demo.services;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegistroRequest;
import com.example.demo.dto.TokenResponse;
import com.example.demo.dto.UsuarioResponse;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.CredencialesInvalidasException;
import com.example.demo.models.Rol;
import com.example.demo.models.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.security.JwtService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public UsuarioResponse registrar(RegistroRequest request) {

        if (usuarioRepository.existsByUsername(request.username())) {
            throw new BusinessException(
                "El username ya está registrado"
            );
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(request.username());
        usuario.setPassword(
            passwordEncoder.encode(request.password())
        );
        usuario.setNombre(request.nombre());
        usuario.setRol(Rol.USER);

        Usuario guardado = usuarioRepository.save(usuario);

        return UsuarioResponse.from(guardado);
    }

    @Transactional(readOnly = true)
    public TokenResponse login(LoginRequest request) {

        Usuario usuario = usuarioRepository
            .findByUsername(request.username())
            .orElseThrow(CredencialesInvalidasException::new);

        if (!passwordEncoder.matches(
                request.password(),
                usuario.getPassword())) {

            throw new CredencialesInvalidasException();
        }

        return new TokenResponse(
            jwtService.generarToken(usuario),
            "Bearer",
            jwtService.getExpiracionSegundos(),
            usuario.getUsername(),
            usuario.getRol()
        );
    }
}