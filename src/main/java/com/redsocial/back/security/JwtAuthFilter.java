package com.redsocial.back.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/**
 * Filtro de autenticación JWT.
 * Intercepta cada solicitud HTTP para verificar el token JWT en el encabezado Authorization.
 * Si el token es válido, establece la autenticación en el contexto de seguridad de Spring.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils utilidadesJwt;
    private final UserDetailsServiceImpl servicioDetallesUsuario;

    public JwtAuthFilter(JwtUtils utilidadesJwt, UserDetailsServiceImpl servicioDetallesUsuario) {
        this.utilidadesJwt = utilidadesJwt;
        this.servicioDetallesUsuario = servicioDetallesUsuario;
    }

    /**
     * Lógica principal del filtro: extrae y valida el token JWT de la solicitud.
     * Si el token es válido, carga los detalles del usuario y establece la autenticación.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest solicitud, HttpServletResponse respuesta, FilterChain cadenaDeFiltros)
            throws ServletException, IOException {
        try {
            String jwt = extraerJwt(solicitud);
            // Validar el token y establecer autenticación si es correcto
            if (jwt != null && utilidadesJwt.validarToken(jwt)) {
                String nombreUsuario = utilidadesJwt.obtenerNombreUsuarioDelToken(jwt);
                UserDetails detallesUsuario = servicioDetallesUsuario.loadUserByUsername(nombreUsuario);
                UsernamePasswordAuthenticationToken autenticacion = new UsernamePasswordAuthenticationToken(
                        detallesUsuario, null, detallesUsuario.getAuthorities());
                autenticacion.setDetails(new WebAuthenticationDetailsSource().buildDetails(solicitud));
                // Registrar la autenticación en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(autenticacion);
            }
        } catch (Exception e) {
            logger.error("No se pudo establecer la autenticación del usuario: {}", e);
        }
        cadenaDeFiltros.doFilter(solicitud, respuesta);
    }

    /**
     * Extrae el token JWT del encabezado Authorization de la solicitud HTTP.
     * El token debe venir con el prefijo "Bearer ".
     *
     * @param solicitud solicitud HTTP entrante
     * @return el token JWT sin el prefijo "Bearer ", o {@code null} si no está presente
     */
    private String extraerJwt(HttpServletRequest solicitud) {
        String encabezadoAuth = solicitud.getHeader("Authorization");
        if (StringUtils.hasText(encabezadoAuth) && encabezadoAuth.startsWith("Bearer ")) {
            return encabezadoAuth.substring(7);
        }
        return null;
    }
}
