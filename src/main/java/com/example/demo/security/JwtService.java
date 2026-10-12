package com.example.demo.security;

import com.example.demo.models.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey clave;
    private final long expiracionMinutos;

    public JwtService(
            @Value("${app.jwt.secret}") String secreto,
            @Value("${app.jwt.expiracion-minutos}") long expiracionMinutos) {

        this.clave = Keys.hmacShaKeyFor(
            secreto.getBytes(StandardCharsets.UTF_8)
        );
        this.expiracionMinutos = expiracionMinutos;
    }

    public String generarToken(Usuario usuario) {
        Instant ahora = Instant.now();

        return Jwts.builder()
            .subject(usuario.getUsername())
            .claim("rol", usuario.getRol().name())
            .issuedAt(Date.from(ahora))
            .expiration(Date.from(
                ahora.plus(expiracionMinutos, ChronoUnit.MINUTES)
            ))
            .signWith(clave)
            .compact();
    }

    public String obtenerUsername(String token) {
        return Jwts.parser()
            .verifyWith(clave)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }

    public long getExpiracionSegundos() {
        return expiracionMinutos * 60;
    }
}