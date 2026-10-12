package com.example.demo.controllers;

import com.example.demo.dto.RecomendacionResponse;
import com.example.demo.services.RecomendacionService;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recomendaciones")
public class RecomendacionController {

    private final RecomendacionService recomendacionService;

    public RecomendacionController(
            RecomendacionService recomendacionService) {

        this.recomendacionService = recomendacionService;
    }

    @GetMapping("/me")
    public List<RecomendacionResponse> recomendar(
            Authentication authentication) {

        return recomendacionService.recomendar(
            authentication.getName()
        );
    }
}