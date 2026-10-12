package com.example.demo.dto;

import com.example.demo.models.Rol;

public record TokenResponse(
    String token,
    String tipo,
    long expiraEnSegundos,
    String username,
    Rol rol
) {}