package com.redsocial.back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Evento de like emitido por WebSocket.
 * Se envía al canal /topic/likes cuando un usuario da o quita like a una publicación,
 * permitiendo que todos los clientes suscritos actualicen su interfaz en tiempo real.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeEvent {

    /** Identificador de la publicación que recibió el like */
    private Long id;

    /** Conteo total de likes actualizado tras la operación */
    private long likeCount;
}
