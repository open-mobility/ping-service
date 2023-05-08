package com.o4.open.mobility.connect;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Slf4j
@Controller
public class WebSocketController {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    private final SimpMessagingTemplate broker;

    public WebSocketController(SimpMessagingTemplate broker) {
        this.broker = broker;
    }

    /* Message will be sent as /app/chat */
    @MessageMapping("/chat")
    /* Message send here will also be sent to the following topic/queue */
    @SendToUser("/queue/greetings")
    public Packet processMessageFromClient(@Payload String message, Principal principal) {
        JSONObject jsonObject = new JSONObject(message);
        String senderName = jsonObject.getString("senderName");
        String chat = jsonObject.getString("chat");
        String otherPerson = jsonObject.getString("person");

        Packet packet = new Packet(senderName + " says: " + chat + "<br />" + IdHelper.getUsername(principal));
        packet.setSenderName(IdHelper.getUsername(principal));
        try {

            if (otherPerson != null && !otherPerson.isEmpty()) {
                packet.setAction("PEER_MESSAGE");
                broker.convertAndSendToUser(otherPerson, "queue/greetings", packet);
            } else {
                log.info("Missed otherPerson is missing");
            }
        } catch (Exception e) {
            log.info("******************************************** Some exception occurred");
            e.printStackTrace();
        }

        packet.setAction("MESSAGE_SELF");
        return packet;
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }

}
