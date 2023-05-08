package com.o4.open.mobility.connect;

import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

public interface IdHelper {

    static String getUsername(SessionDisconnectEvent event) {
        return getUsername(event.getUser());
    }

    static String getUsername(Principal principal) {
        if (principal instanceof SocketPrincipal socketPrincipal) {
            return socketPrincipal.getDisplayName() + " ("+socketPrincipal.getName()+")";
        }

        return "Name can't be identified";
    }

    static String getUsername(SessionConnectEvent event) {
        return getUsername(event.getUser());
    }
}
