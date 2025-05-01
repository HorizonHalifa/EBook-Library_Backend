package com.horizon.ebooklibrary.ebooklibrarybackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * Configures WebSocket support for the application using STOMP protocol and implementing SockJS fallback
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Register the endpoint that users will use to connect to the WebSocket server.
     * The registry will be connected to the /ws using SockJS
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // Allow all origins (TODO: For development only, change soontm)
                .withSockJS(); // Enable SockJS fallback for browsers without native WebSockets
    }

    /**
     * Configure the message broker to use the /topic prefix for broadcasting messages.
     * Tells Spring to route messages with /topic to the in-memory message broker.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic"); // Enable simple in-memory broker
        registry.setApplicationDestinationPrefixes("/app"); // Prefix for messages sent from client to server
    }
}
