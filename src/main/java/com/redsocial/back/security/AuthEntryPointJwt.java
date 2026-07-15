package com.redsocial.back.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * Punto de entrada para errores de autenticación JWT.
 * Se invoca cuando un usuario no autenticado intenta acceder a un recurso protegido.
 * Responde con el código HTTP 401 (No Autorizado).
 */
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    /**
     * Maneja el error de acceso no autorizado enviando una respuesta HTTP 401.
     *
     * @param solicitud       solicitud HTTP que generó el error
     * @param respuesta       respuesta HTTP donde se envía el error
     * @param excepcionAuth   excepción de autenticación que causó el rechazo
     */
    @Override
    public void commence(HttpServletRequest solicitud, HttpServletResponse respuesta, AuthenticationException excepcionAuth)
            throws IOException {
        respuesta.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: No autorizado");
    }
}
