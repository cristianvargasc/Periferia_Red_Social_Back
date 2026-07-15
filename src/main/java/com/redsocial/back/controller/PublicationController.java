package com.redsocial.back.controller;

import com.redsocial.back.dto.PublicacionCreateRequest;
import com.redsocial.back.dto.PublicacionDto;
import com.redsocial.back.model.Publicacion;
import com.redsocial.back.model.Usuario;
import com.redsocial.back.repository.LikeRepository;
import com.redsocial.back.repository.PublicacionRepository;
import com.redsocial.back.repository.UsuarioRepository;
import com.redsocial.back.security.UserDetailsImpl;
import com.redsocial.back.service.LikeServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador de publicaciones.
 * Gestiona la creación, listado y gestión de likes de las publicaciones de los usuarios.
 */
@RestController
@RequestMapping("/api/publications")
@Tag(name = "Publicaciones", description = "Endpoints para publicaciones y likes")
public class PublicationController {

    /** Logger de auditoría para registrar operaciones sobre publicaciones */
    private static final Logger registroAuditoria = LoggerFactory.getLogger("audit.logger");

    private final PublicacionRepository repositorioPublicacion;
    private final UsuarioRepository repositorioUsuario;
    private final LikeRepository repositorioLike;
    private final LikeServicio likeServicio;

    public PublicationController(PublicacionRepository repositorioPublicacion,
                                 UsuarioRepository repositorioUsuario,
                                 LikeRepository repositorioLike,
                                 LikeServicio likeServicio) {
        this.repositorioPublicacion = repositorioPublicacion;
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioLike = repositorioLike;
        this.likeServicio = likeServicio;
    }

    /**
     * Lista las publicaciones de otros usuarios (excluyendo las del usuario autenticado),
     * ordenadas por fecha de publicación descendente.
     *
     * @param detallesUsuario datos del usuario autenticado inyectados por Spring Security
     * @return lista de publicaciones con conteo de likes y estado de like del usuario
     */
    @GetMapping
    @Operation(summary = "Listar publicaciones de otros usuarios")
    public ResponseEntity<List<PublicacionDto>> listarPublicaciones(@AuthenticationPrincipal UserDetailsImpl detallesUsuario) {
        List<Publicacion> publicaciones = repositorioPublicacion
                .findByUsuarioIdNotOrderByFechaPublicacionDesc(detallesUsuario.getId());

        // Transformar entidades a DTOs con conteo de likes y estado de like del usuario
        List<PublicacionDto> dtos = publicaciones.stream().map(publicacion -> {
            long cantidadLikes = repositorioLike.countByPublicacionId(publicacion.getId());
            boolean yaLeDioLike = repositorioLike.existsByUsuarioIdAndPublicacionId(
                    detallesUsuario.getId(), publicacion.getId());

            return new PublicacionDto(
                    publicacion.getId(),
                    publicacion.getMensaje(),
                    publicacion.getFechaPublicacion(),
                    publicacion.getUsuario().getId(),
                    publicacion.getUsuario().getAlias(),
                    publicacion.getUsuario().getNombres(),
                    publicacion.getUsuario().getApellidos(),
                    cantidadLikes,
                    yaLeDioLike
            );
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    /**
     * Crea una nueva publicación para el usuario autenticado.
     *
     * @param detallesUsuario datos del usuario autenticado
     * @param solicitud       cuerpo de la solicitud con el mensaje de la publicación
     * @return la publicación creada como DTO
     */
    @PostMapping
    @Operation(summary = "Crear una nueva publicación")
    public ResponseEntity<PublicacionDto> crearPublicacion(
            @AuthenticationPrincipal UserDetailsImpl detallesUsuario,
            @RequestBody PublicacionCreateRequest solicitud) {

        registroAuditoria.info("Auditoría: El usuario {} está creando una publicación: {}",
                detallesUsuario.getUsername(), solicitud.getMensaje());

        Usuario usuario = repositorioUsuario.findById(detallesUsuario.getId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Construir y guardar la nueva publicación
        Publicacion nuevaPublicacion = new Publicacion();
        nuevaPublicacion.setMensaje(solicitud.getMensaje());
        nuevaPublicacion.setUsuario(usuario);

        Publicacion publicacionGuardada = repositorioPublicacion.save(nuevaPublicacion);

        // Recuperar la publicación completa para incluir la fecha generada por la base de datos
        Publicacion publicacionCompleta = repositorioPublicacion
                .findById(publicacionGuardada.getId()).orElse(publicacionGuardada);

        PublicacionDto dto = new PublicacionDto(
                publicacionCompleta.getId(),
                publicacionCompleta.getMensaje(),
                publicacionCompleta.getFechaPublicacion(),
                usuario.getId(),
                usuario.getAlias(),
                usuario.getNombres(),
                usuario.getApellidos(),
                0,
                false
        );

        registroAuditoria.info("Auditoría: Publicación creada exitosamente con ID {}", publicacionCompleta.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    /**
     * Alterna el like de una publicación para el usuario autenticado.
     *
     * @param detallesUsuario datos del usuario autenticado
     * @param id              identificador de la publicación
     * @return 200 OK si fue exitoso, 404 si la publicación no existe
     */
    @PostMapping("/{id}/like")
    @Operation(summary = "Alternar like de una publicación (usa el procedimiento almacenado alternar_like)")
    public ResponseEntity<Void> alternarLike(
            @AuthenticationPrincipal UserDetailsImpl detallesUsuario,
            @PathVariable("id") Long id) {

        registroAuditoria.info("Auditoría: El usuario {} solicita alternar like para la publicación con ID {}",
                detallesUsuario.getUsername(), id);

        boolean procesado = likeServicio.alternarLike(detallesUsuario.getId(), id);

        if (!procesado) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }
}
