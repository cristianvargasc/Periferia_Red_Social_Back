package com.redsocial.back.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configuración del broker WebSocket con protocolo STOMP.
 * Habilita la comunicación en tiempo real entre el servidor y los clientes.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Configura el broker de mensajes.
     * - Habilita el broker simple para el prefijo /topic (suscripciones de eventos en tiempo real)
     * - Define el prefijo /app para comandos enviados desde el cliente
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry configuracion) {
        // Prefijo de destino para suscribirse a eventos en tiempo real
        configuracion.enableSimpleBroker("/topic");
        // Prefijo de destino para comandos del cliente hacia la aplicación
        configuracion.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Registra los endpoints WebSocket al que se conectan los clientes.
     * - /ws:        con SockJS como respaldo (para compatibilidad general)
     * - /ws-native: WebSocket nativo sin SockJS (para clientes Angular modernos)
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registro) {
        // Endpoint con SockJS para navegadores sin soporte nativo de WebSocket
        registro.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();

        // Endpoint WebSocket nativo — permite conexión sin la librería sockjs-client
        registro.addEndpoint("/ws-native")
                .setAllowedOriginPatterns("*");
    }
}
