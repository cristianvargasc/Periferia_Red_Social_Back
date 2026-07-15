package com.redsocial.back.controller;

import com.redsocial.back.model.Usuario;
import com.redsocial.back.repository.UsuarioRepository;
import com.redsocial.back.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador de usuarios.
 * Expone endpoints para consultar el perfil del usuario autenticado.
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuarios", description = "Endpoints para perfiles de usuario")
public class UserController {

    private final UsuarioRepository repositorioUsuario;

    public UserController(UsuarioRepository repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    /**
     * Obtiene el perfil del usuario actualmente autenticado.
     *
     * @param detallesUsuario datos del usuario autenticado inyectados por Spring Security
     * @return datos del perfil del usuario o 404 si no se encuentra
     */
    @GetMapping("/profile")
    @Operation(summary = "Obtener el perfil del usuario autenticado")
    public ResponseEntity<Usuario> obtenerPerfil(@AuthenticationPrincipal UserDetailsImpl detallesUsuario) {
        return repositorioUsuario.findById(detallesUsuario.getId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
