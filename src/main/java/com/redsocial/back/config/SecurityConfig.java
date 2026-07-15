package com.redsocial.back.config;

import com.redsocial.back.security.AuthEntryPointJwt;
import com.redsocial.back.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.Collections;

/**
 * Configuración de seguridad de la aplicación.
 * Define las reglas de acceso, el manejo de sesión sin estado (stateless),
 * la configuración de CORS y el filtro JWT.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    /** Punto de entrada para manejo de errores de autenticación no autorizada */
    private final AuthEntryPointJwt manejadorNoAutorizado;

    /** Filtro JWT que valida el token en cada solicitud HTTP */
    private final JwtAuthFilter filtroJwt;

    public SecurityConfig(AuthEntryPointJwt manejadorNoAutorizado, JwtAuthFilter filtroJwt) {
        this.manejadorNoAutorizado = manejadorNoAutorizado;
        this.filtroJwt = filtroJwt;
    }

    /**
     * Expone el gestor de autenticación como bean de Spring.
     */
    @Bean
    public AuthenticationManager gestorAutenticacion(AuthenticationConfiguration configuracionAuth) throws Exception {
        return configuracionAuth.getAuthenticationManager();
    }

    /**
     * Codificador de contraseñas.
     * NOTA: Se usa texto plano para coincidir con los valores de la base de datos semilla (password123).
     * En producción se debe usar BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder codificadorContrasena() {
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * Configura la cadena de filtros de seguridad HTTP.
     * - Habilita CORS con la configuración personalizada
     * - Deshabilita CSRF (API REST sin estado)
     * - Gestión de sesión sin estado (STATELESS)
     * - Permite acceso libre a endpoints de autenticación, documentación y WebSocket
     * - Requiere autenticación para cualquier otra ruta
     */
    @Bean
    public SecurityFilterChain cadenaFiltrosSeguidad(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(fuenteConfiguracionCors()))
            .csrf(AbstractHttpConfigurer::disable)
            .exceptionHandling(excepcion -> excepcion.authenticationEntryPoint(manejadorNoAutorizado))
            .sessionManagement(sesion -> sesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(autorizacion -> autorizacion
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/docs", "/swagger-ui/**", "/v3/api-docs/**", "/api-docs/**").permitAll()
                .requestMatchers("/ws/**").permitAll() // Handshake WebSocket
                .anyRequest().authenticated()
            );

        // Agregar el filtro JWT antes del filtro de autenticación estándar de Spring
        http.addFilterBefore(filtroJwt, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Configura la política CORS para permitir solicitudes desde el cliente local.
     */
    @Bean
    public CorsConfigurationSource fuenteConfiguracionCors() {
        CorsConfiguration configuracion = new CorsConfiguration();
        // Permitir cualquier origen (para desarrollo local)
        configuracion.setAllowedOriginPatterns(Collections.singletonList("*"));
        configuracion.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuracion.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
        configuracion.setExposedHeaders(Collections.singletonList("Authorization"));
        configuracion.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource fuente = new UrlBasedCorsConfigurationSource();
        fuente.registerCorsConfiguration("/**", configuracion);
        return fuente;
    }
}
