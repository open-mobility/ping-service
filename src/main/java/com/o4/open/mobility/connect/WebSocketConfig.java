package com.o4.open.mobility.connect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private static final String EXPOSED_URL_FOR_CONNECTION = "/ws-connect";

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        /**
         * Configure in-memory message broker to carry the messages back to the client
         * on destinations prefixed with /topic and /queue
         */
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        log.info("\n-----------------------------------------------------\n\n"
        + "Registering end points for connections..............\n"
        + "These URLs will be used to connect for web socket\n"
        + "Check code for exact URL it can be changed\n"
        + "url -> " +  EXPOSED_URL_FOR_CONNECTION + "\n"
        + "\n\n-----------------------------------------------------\n");

        registry.addEndpoint(EXPOSED_URL_FOR_CONNECTION)
                .setAllowedOrigins("*")
                .setHandshakeHandler(new ClientHandshakeHandler());

        registry.addEndpoint(EXPOSED_URL_FOR_CONNECTION)
                .setAllowedOrigins("*")
                .setHandshakeHandler(new ClientHandshakeHandler())
                .withSockJS();
    }

}