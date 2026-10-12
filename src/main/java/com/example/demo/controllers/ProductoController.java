package com.example.demo.controllers;

import com.example.demo.dto.ProductoRequest;
import com.example.demo.dto.ProductoResponse;
import com.example.demo.models.CategoriaProducto;
import com.example.demo.services.ProductoService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    public ResponseEntity<ProductoResponse> crear(
            @Valid @RequestBody ProductoRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(productoService.crear(request));
    }

    @GetMapping
    public List<ProductoResponse> listar(
            @RequestParam(required = false)
            CategoriaProducto categoria) {

        return productoService.listar(categoria);
    }

    @GetMapping("/{id}")
    public ProductoResponse obtenerPorId(@PathVariable Long id) {
        return productoService.obtenerPorId(id);
    }
}