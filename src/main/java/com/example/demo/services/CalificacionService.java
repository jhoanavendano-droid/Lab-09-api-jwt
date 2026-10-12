package com.example.demo.services;

import com.example.demo.dto.CalificacionRequest;
import com.example.demo.dto.CalificacionResponse;
import com.example.demo.exception.AccesoDenegadoException;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.models.Calificacion;
import com.example.demo.models.Producto;
import com.example.demo.models.Usuario;
import com.example.demo.repository.CalificacionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CalificacionService {

    private final CalificacionRepository calificacionRepository;
    private final UsuarioService usuarioService;
    private final ProductoService productoService;

    public CalificacionService(
            CalificacionRepository calificacionRepository,
            UsuarioService usuarioService,
            ProductoService productoService) {

        this.calificacionRepository = calificacionRepository;
        this.usuarioService = usuarioService;
        this.productoService = productoService;
    }

    public CalificacionResponse crear(
            String username,
            CalificacionRequest request) {

        Usuario usuario = usuarioService.buscarPorUsername(username);
        Producto producto = productoService.buscarPorId(
            request.productoId()
        );

        boolean yaCalifico = calificacionRepository
            .existsByUsuarioIdAndProductoId(
                usuario.getId(),
                producto.getId()
            );

        if (yaCalifico) {
            throw new BusinessException(
                "Ya calificaste el producto " + producto.getNombre()
            );
        }

        Calificacion calificacion = new Calificacion();

        calificacion.setUsuario(usuario);
        calificacion.setProducto(producto);
        calificacion.setPuntuacion(request.puntuacion());
        calificacion.setComentario(request.comentario());

        return CalificacionResponse.from(
            calificacionRepository.save(calificacion)
        );
    }

    @Transactional(readOnly = true)
    public List<CalificacionResponse> listarMias(String username) {

        Usuario usuario = usuarioService.buscarPorUsername(username);

        return calificacionRepository
            .findByUsuarioIdOrderByFechaDesc(usuario.getId())
            .stream()
            .map(CalificacionResponse::from)
            .toList();
    }

    public void eliminar(
            Long id,
            String username,
            boolean esAdmin) {

        Calificacion calificacion = calificacionRepository
            .findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("Calificación", id)
            );

        boolean esDueno = calificacion.getUsuario()
            .getUsername()
            .equals(username);

        if (!esDueno && !esAdmin) {
            throw new AccesoDenegadoException(
                "Solo puedes eliminar tus calificaciones"
            );
        }

        calificacionRepository.delete(calificacion);
    }
}