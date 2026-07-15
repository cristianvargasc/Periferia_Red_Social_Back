package com.redsocial.back.repository;

import com.redsocial.back.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link Usuario}.
 * Provee operaciones de búsqueda y el procedimiento almacenado para registrar nuevos usuarios.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username nombre de usuario a buscar
     * @return optional con el usuario encontrado, o vacío si no existe
     */
    Optional<Usuario> findByUsername(String username);

    /**
     * Busca un usuario por su alias público.
     *
     * @param alias alias del usuario a buscar
     * @return optional con el usuario encontrado, o vacío si no existe
     */
    Optional<Usuario> findByAlias(String alias);

    /**
     * Llama al procedimiento almacenado {@code registrar_usuario_perfil}
     * para registrar un nuevo usuario con su información de perfil.
     *
     * @param username        nombre de usuario único
     * @param password        contraseña del usuario
     * @param nombres         nombres del usuario
     * @param apellidos       apellidos del usuario
     * @param fechaNacimiento fecha de nacimiento
     * @param alias           alias público único en la red social
     */
    @Procedure(procedureName = "registrar_usuario_perfil")
    void registrarUsuarioPerfil(
        @Param("p_username") String username,
        @Param("p_password") String password,
        @Param("p_nombres") String nombres,
        @Param("p_apellidos") String apellidos,
        @Param("p_fecha_nacimiento") LocalDate fechaNacimiento,
        @Param("p_alias") String alias
    );
}
