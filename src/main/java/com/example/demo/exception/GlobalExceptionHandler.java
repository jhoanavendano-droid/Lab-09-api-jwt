package com.example.demo.exception;

import com.example.demo.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> notFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        return build(
            HttpStatus.NOT_FOUND,
            ex.getMessage(),
            request,
            null
        );
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> business(
            BusinessException ex,
            HttpServletRequest request) {

        return build(
            HttpStatus.CONFLICT,
            ex.getMessage(),
            request,
            null
        );
    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<ErrorResponse> credenciales(
            CredencialesInvalidasException ex,
            HttpServletRequest request) {

        return build(
            HttpStatus.UNAUTHORIZED,
            ex.getMessage(),
            request,
            null
        );
    }

    @ExceptionHandler(AccesoDenegadoException.class)
    public ResponseEntity<ErrorResponse> accesoDenegado(
            AccesoDenegadoException ex,
            HttpServletRequest request) {

        return build(
            HttpStatus.FORBIDDEN,
            ex.getMessage(),
            request,
            null
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> detalles = new LinkedHashMap<>();

        ex.getBindingResult()
            .getFieldErrors()
            .forEach(error -> detalles.putIfAbsent(
                error.getField(),
                error.getDefaultMessage()
            ));

        return build(
            HttpStatus.BAD_REQUEST,
            "Datos inválidos",
            request,
            detalles
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> unreadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        return build(
            HttpStatus.BAD_REQUEST,
            "JSON mal formado o valor no permitido; revise la categoría",
            request,
            null
        );
    }

    private ResponseEntity<ErrorResponse> build(
            HttpStatus status,
            String mensaje,
            HttpServletRequest request,
            Map<String, String> detalles) {

        ErrorResponse respuesta = new ErrorResponse(
            LocalDateTime.now(),
            status.value(),
            status.getReasonPhrase(),
            mensaje,
            request.getRequestURI(),
            detalles
        );

        return ResponseEntity.status(status).body(respuesta);
    }
}