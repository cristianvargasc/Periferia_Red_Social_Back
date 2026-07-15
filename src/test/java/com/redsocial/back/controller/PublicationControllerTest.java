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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias para {@link PublicationController}.
 */
class PublicationControllerTest {

    @Mock
    private PublicacionRepository publicacionRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private LikeRepository likeRepository;

    /**
     * El controlador ahora usa LikeServicio en lugar de SimpMessagingTemplate directamente.
     * SimpMessagingTemplate está encapsulado dentro de LikeServicio.
     */
    @Mock
    private LikeServicio likeServicio;

    @InjectMocks
    private PublicationController publicationController;

    private UserDetailsImpl userDetails;
    private Usuario user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new Usuario();
        user.setId(1L);
        user.setUsername("testuser");
        user.setNombres("John");
        user.setApellidos("Doe");
        user.setAlias("johndoe");
        userDetails = new UserDetailsImpl(user);
    }

    /**
     * Verifica que la creación de una publicación devuelva 201 CREATED
     * con el DTO correcto en el cuerpo de la respuesta.
     * Nota: el método en el controlador se llama crearPublicacion().
     */
    @Test
    void testCrearPublicacion() {
        PublicacionCreateRequest solicitud = new PublicacionCreateRequest();
        solicitud.setMensaje("New Post content");

        Publicacion publicacion = new Publicacion();
        publicacion.setId(10L);
        publicacion.setMensaje("New Post content");
        publicacion.setUsuario(user);
        publicacion.setFechaPublicacion(LocalDateTime.now());

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(user));
        when(publicacionRepository.save(any(Publicacion.class))).thenReturn(publicacion);
        when(publicacionRepository.findById(10L)).thenReturn(Optional.of(publicacion));

        ResponseEntity<PublicacionDto> respuesta = publicationController.crearPublicacion(userDetails, solicitud);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertEquals("New Post content", respuesta.getBody().getMensaje());
        assertEquals(1L, respuesta.getBody().getUsuarioId());
    }
}
