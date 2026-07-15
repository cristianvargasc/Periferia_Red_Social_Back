package com.redsocial.back.controller;

import com.redsocial.back.dto.LoginResponse;
import com.redsocial.back.model.Usuario;
import com.redsocial.back.repository.UsuarioRepository;
import com.redsocial.back.security.JwtUtils;
import com.redsocial.back.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador de autenticación.
 * Gestiona el inicio de sesión de usuarios mediante JWT.
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Endpoints para la autenticación de usuarios")
public class AuthController {

    /** Logger de auditoría para registrar accesos al sistema */
    private static final Logger registroAuditoria = LoggerFactory.getLogger("audit.logger");

    private final AuthenticationManager gestorAutenticacion;
    private final JwtUtils utilidadesJwt;
    private final UsuarioRepository repositorioUsuario;

    public AuthController(AuthenticationManager gestorAutenticacion, JwtUtils utilidadesJwt, UsuarioRepository repositorioUsuario) {
        this.gestorAutenticacion = gestorAutenticacion;
        this.utilidadesJwt = utilidadesJwt;
        this.repositorioUsuario = repositorioUsuario;
    }

    /**
     * Inicia sesión con usuario y contraseña mediante parámetros GET.
     * Devuelve un token JWT junto con los datos básicos del usuario autenticado.
     *
     * @param nombreUsuario nombre de usuario registrado en el sistema
     * @param contrasena    contraseña del usuario
     * @return respuesta con token JWT y datos del usuario
     */
    @GetMapping("/login")
    @Operation(summary = "Iniciar sesión con JWT usando parámetros GET")
    public ResponseEntity<LoginResponse> iniciarSesion(
            @RequestParam("username") String nombreUsuario,
            @RequestParam("password") String contrasena) {

        registroAuditoria.info("Auditoría: Intento de inicio de sesión para el usuario '{}'", nombreUsuario);

        // Autenticar al usuario con las credenciales proporcionadas
        Authentication autenticacion = gestorAutenticacion.authenticate(
                new UsernamePasswordAuthenticationToken(nombreUsuario, contrasena));

        // Establecer la autenticación en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(autenticacion);
        String tokenJwt = utilidadesJwt.generarToken(nombreUsuario);

        // Recuperar detalles completos del usuario autenticado
        UserDetailsImpl detallesUsuario = (UserDetailsImpl) autenticacion.getPrincipal();
        Usuario usuario = repositorioUsuario.findById(detallesUsuario.getId()).orElseThrow();

        registroAuditoria.info("Auditoría: Inicio de sesión exitoso para el usuario '{}' con ID {}",
                nombreUsuario, detallesUsuario.getId());

        return ResponseEntity.ok(new LoginResponse(
                tokenJwt,
                usuario.getId(),
                usuario.getUsername(),
                usuario.getAlias(),
                usuario.getNombres(),
                usuario.getApellidos(),
                usuario.getFechaNacimiento()
        ));
    }
}
