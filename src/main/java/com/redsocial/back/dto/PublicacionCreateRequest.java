package com.redsocial.back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Objeto de transferencia de datos para la creación de publicaciones.
 * Contiene el mensaje que el usuario desea publicar en la red social.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicacionCreateRequest {

    /** Texto del mensaje de la publicación */
    private String mensaje;
}
