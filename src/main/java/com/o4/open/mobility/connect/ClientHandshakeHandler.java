package com.o4.open.mobility.connect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class ClientHandshakeHandler extends DefaultHandshakeHandler {
    @Override
    protected Principal determineUser(ServerHttpRequest req, WebSocketHandler weHandler, Map<String, Object> attributes) {
        log.info("**************** determineUser..............");

        try {
            log.debug(req.getBody().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String username;

        // Not able to get this value its empty
        if (req.getHeaders().containsKey("username")) {
            username = req.getHeaders().get("username").toString();
        }
        username = "No name";

        final String randId = UUID.randomUUID().toString();
        log.info("{}", attributes.get("name"));
        log.info("User opened client unique ID {}, ipAddress {}", randId, req.getRemoteAddress());

        return new SocketPrincipal(randId, username);
    }
}
