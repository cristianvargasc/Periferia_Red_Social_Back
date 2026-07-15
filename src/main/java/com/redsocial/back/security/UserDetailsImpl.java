package com.redsocial.back.security;

import com.redsocial.back.model.Usuario;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

/**
 * Implementación de {@link UserDetails} para integrar el modelo {@link Usuario}
 * con el sistema de seguridad de Spring Security.
 * Encapsula los datos del usuario necesarios para la autenticación y autorización.
 */
@Getter
public class UserDetailsImpl implements UserDetails {

    /** Identificador único del usuario en la base de datos */
    private final Long id;

    /** Nombre de usuario para autenticación */
    private final String username;

    /** Contraseña del usuario (gestionada por Spring Security) */
    private final String password;

    /** Alias público del usuario en la red social */
    private final String alias;

    /** Nombres del usuario */
    private final String nombres;

    /** Apellidos del usuario */
    private final String apellidos;

    /**
     * Constructor que crea una instancia a partir de la entidad {@link Usuario}.
     *
     * @param usuario entidad de usuario desde la base de datos
     */
    public UserDetailsImpl(Usuario usuario) {
        this.id = usuario.getId();
        this.username = usuario.getUsername();
        this.password = usuario.getPassword();
        this.alias = usuario.getAlias();
        this.nombres = usuario.getNombres();
        this.apellidos = usuario.getApellidos();
    }

    /** No se manejan roles por ahora; se retorna lista vacía */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    /** La cuenta nunca expira en esta implementación */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /** La cuenta nunca se bloquea en esta implementación */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /** Las credenciales nunca expiran en esta implementación */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /** El usuario siempre está habilitado en esta implementación */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
