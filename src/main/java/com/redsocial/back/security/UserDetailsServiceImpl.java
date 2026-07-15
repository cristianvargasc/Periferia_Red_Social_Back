package com.redsocial.back.security;

import com.redsocial.back.model.Usuario;
import com.redsocial.back.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementación del servicio de carga de detalles de usuario.
 * Spring Security utiliza este servicio para obtener los datos del usuario
 * durante el proceso de autenticación.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository repositorioUsuario;

    public UserDetailsServiceImpl(UsuarioRepository repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    /**
     * Carga los detalles del usuario por su nombre de usuario.
     * Envuelve la entidad {@link Usuario} en un {@link UserDetailsImpl} compatible con Spring Security.
     *
     * @param nombreUsuario nombre de usuario a buscar en la base de datos
     * @return detalles del usuario encontrado
     * @throws UsernameNotFoundException si no existe ningún usuario con ese nombre
     */
    @Override
    public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {
        Usuario usuario = repositorioUsuario.findByUsername(nombreUsuario)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado con el nombre de usuario: " + nombreUsuario));
        return new UserDetailsImpl(usuario);
    }
}
