package com.redsocial.back.repository;

import com.redsocial.back.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad {@link Like}.
 * Provee operaciones de consulta y el procedimiento almacenado para alternar likes.
 */
@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    /**
     * Cuenta la cantidad de likes de una publicación específica.
     *
     * @param publicacionId identificador de la publicación
     * @return total de likes de la publicación
     */
    long countByPublicacionId(Long publicacionId);

    /**
     * Verifica si un usuario ya le ha dado like a una publicación.
     *
     * @param usuarioId     identificador del usuario
     * @param publicacionId identificador de la publicación
     * @return {@code true} si el usuario ya dio like; {@code false} en caso contrario
     */
    boolean existsByUsuarioIdAndPublicacionId(Long usuarioId, Long publicacionId);

    /**
     * Llama al procedimiento almacenado {@code alternar_like} que agrega o quita un like
     * dependiendo del estado actual del usuario en esa publicación.
     *
     * @param usuarioId     identificador del usuario (como entero)
     * @param publicacionId identificador de la publicación (como entero)
     */
    @Procedure(procedureName = "alternar_like")
    void alternarLike(
        @Param("p_usuario_id") Integer usuarioId,
        @Param("p_publicacion_id") Integer publicacionId
    );
}
