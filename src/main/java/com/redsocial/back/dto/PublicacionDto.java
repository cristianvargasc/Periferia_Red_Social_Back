package com.redsocial.back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Objeto de transferencia de datos para la representación de publicaciones.
 * Incluye los datos de la publicación, el autor y el estado de likes del usuario autenticado.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicacionDto {

    /** Identificador único de la publicación */
    private Long id;

    /** Contenido textual de la publicación */
    private String mensaje;

    /** Fecha y hora en que se realizó la publicación */
    private LocalDateTime fechaPublicacion;

    /** Identificador del usuario autor de la publicación */
    private Long usuarioId;

    /** Alias del usuario autor */
    private String usuarioAlias;

    /** Nombres del usuario autor */
    private String usuarioNombres;

    /** Apellidos del usuario autor */
    private String usuarioApellidos;

    /** Cantidad total de likes que tiene la publicación */
    private long likeCount;

    /** Indica si el usuario autenticado actualmente le ha dado like a la publicación */
    private boolean likedByCurrentUser;
}
