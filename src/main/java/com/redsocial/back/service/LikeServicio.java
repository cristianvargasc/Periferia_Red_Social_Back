package com.redsocial.back.service;

import com.redsocial.back.dto.LikeEvent;
import com.redsocial.back.repository.LikeRepository;
import com.redsocial.back.repository.PublicacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Servicio de likes.
 * Encapsula la lógica transaccional del toggle de likes,
 * separándola del controlador para evitar problemas de proxy CGLIB
 * con Spring Security en el contexto de @EnableMethodSecurity.
 */
@Service
public class LikeServicio {

    private static final Logger registroAuditoria = LoggerFactory.getLogger("audit.logger");

    private final LikeRepository repositorioLike;
    private final PublicacionRepository repositorioPublicacion;
    private final SimpMessagingTemplate plantillaMensajeria;

    public LikeServicio(LikeRepository repositorioLike,
                        PublicacionRepository repositorioPublicacion,
                        SimpMessagingTemplate plantillaMensajeria) {
        this.repositorioLike = repositorioLike;
        this.repositorioPublicacion = repositorioPublicacion;
        this.plantillaMensajeria = plantillaMensajeria;
    }

    /**
     * Alterna el like de una publicación llamando al procedimiento almacenado
     * {@code alternar_like} y notificando a los clientes vía WebSocket.
     *
     * <p>NOTA: No se usa @Transactional aquí porque el procedimiento almacenado
     * de PostgreSQL contiene COMMIT explícito y no puede ejecutarse dentro de
     * una transacción activa de Spring.</p>
     *
     * @param usuarioId     ID del usuario que da o quita el like
     * @param publicacionId ID de la publicación objetivo
     * @return {@code true} si la publicación existe y se procesó, {@code false} si no existe
     */
    public boolean alternarLike(Long usuarioId, Long publicacionId) {
        // Verificar que la publicación exista antes de procesar el like
        if (!repositorioPublicacion.existsById(publicacionId)) {
            return false;
        }

        registroAuditoria.info("Auditoría: Alternando like — usuario ID {} → publicación ID {}",
                usuarioId, publicacionId);

        // Llamar al procedimiento almacenado (sin transacción activa de Spring)
        repositorioLike.alternarLike(usuarioId.intValue(), publicacionId.intValue());

        // Obtener el conteo actualizado de likes tras el cambio
        long conteoActualizado = repositorioLike.countByPublicacionId(publicacionId);

        // Notificar en tiempo real a los clientes suscritos vía STOMP/WebSocket
        plantillaMensajeria.convertAndSend("/topic/likes", new LikeEvent(publicacionId, conteoActualizado));

        registroAuditoria.info("Auditoría: Like alternado para publicación ID {}. Nuevo conteo: {}",
                publicacionId, conteoActualizado);

        return true;
    }
}
