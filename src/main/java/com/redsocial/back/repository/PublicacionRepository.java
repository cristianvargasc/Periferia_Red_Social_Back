package com.redsocial.back.repository;

import com.redsocial.back.model.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositorio JPA para la entidad {@link Publicacion}.
 * Provee operaciones de consulta sobre la tabla de publicaciones.
 */
@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {

    /**
     * Obtiene todas las publicaciones de usuarios distintos al especificado,
     * ordenadas por fecha de publicación de forma descendente (más recientes primero).
     *
     * @param userId identificador del usuario a excluir
     * @return lista de publicaciones de otros usuarios
     */
    @Query("SELECT p FROM Publicacion p WHERE p.usuario.id <> :userId ORDER BY p.fechaPublicacion DESC")
    List<Publicacion> findByUsuarioIdNotOrderByFechaPublicacionDesc(@Param("userId") Long userId);
}
