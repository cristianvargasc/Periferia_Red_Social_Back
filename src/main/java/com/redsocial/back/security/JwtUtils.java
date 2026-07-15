package com.redsocial.back.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

/**
 * Utilidades para la gestión de tokens JWT.
 * Encapsula la generación, validación y extracción de información de los tokens.
 */
@Component
public class JwtUtils {

    private static final Logger registrador = LoggerFactory.getLogger(JwtUtils.class);

    /** Clave secreta para firmar y verificar los tokens JWT */
    @Value("${app.jwt.secret}")
    private String secretoJwt;

    /** Tiempo de expiración del token en milisegundos */
    @Value("${app.jwt.expiration}")
    private int tiempoExpiracionMs;

    /**
     * Obtiene la clave de firma HMAC a partir de la clave secreta configurada.
     */
    private Key obtenerClaveDeriva() {
        return Keys.hmacShaKeyFor(secretoJwt.getBytes());
    }

    /**
     * Genera un token JWT para el usuario especificado.
     *
     * @param nombreUsuario nombre de usuario que se establece como sujeto del token
     * @return token JWT firmado en formato compacto
     */
    public String generarToken(String nombreUsuario) {
        return Jwts.builder()
                .setSubject(nombreUsuario)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + tiempoExpiracionMs))
                .signWith(obtenerClaveDeriva(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extrae el nombre de usuario (sujeto) del token JWT.
     *
     * @param token token JWT a analizar
     * @return nombre de usuario contenido en el token
     */
    public String obtenerNombreUsuarioDelToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(obtenerClaveDeriva())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Valida la integridad y vigencia del token JWT.
     *
     * @param tokenAuth token JWT a validar
     * @return {@code true} si el token es válido; {@code false} en caso contrario
     */
    public boolean validarToken(String tokenAuth) {
        try {
            Jwts.parserBuilder().setSigningKey(obtenerClaveDeriva()).build().parseClaimsJws(tokenAuth);
            return true;
        } catch (MalformedJwtException e) {
            registrador.error("Token JWT malformado: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            registrador.error("Token JWT expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            registrador.error("Token JWT no soportado: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            registrador.error("Las claims del JWT están vacías: {}", e.getMessage());
        }
        return false;
    }
}
