package com.o4.open.mobility.connect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class WebSocketEventListener {
    private final Set<String> onlineUsers = new HashSet<>();

    private final SimpMessagingTemplate broker;

    public WebSocketEventListener(SimpMessagingTemplate broker) {
        this.broker = broker;
    }

    @EventListener
    public void onDisconnectEvent(SessionDisconnectEvent event) {
        String username = IdHelper.getUsername(event);
        log.info("[[[ - DISCONNECT - ]]] : {}", username);
        if (event.getUser() != null) {
            onlineUsers.remove(username);
            broker.convertAndSend("/topic/greetings", new Packet(username + " ==> DC", "USER_DISCONNECTED"));
        } else {
            log.error("[[[= Disconnect error - ]]] - User object was null");
        }

    }

    @EventListener
    private void handleSessionConnected(SessionConnectEvent event) {

        String username = IdHelper.getUsername(event);
        log.info("[[[ - CONNECT - ]]] : {}", username);

        registerUser(username);
    }

    private void registerUser(String name) {
        if (null == name || name.isEmpty()) {
            return;
        }

        if (!onlineUsers.contains(name)) {
            log.info("Registering new users...... {}", name);
            onlineUsers.add(name);

            broker.convertAndSend("/topic/greetings", new Packet(name, "USER_JOINED"));
        }
    }

}