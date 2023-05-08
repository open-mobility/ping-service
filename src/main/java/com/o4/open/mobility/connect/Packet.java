package com.o4.open.mobility.connect;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class Packet {
    private String action;
    private String senderName;
    private String senderId;
    private String receiverId;
    private String message;
    private Map<String, String> data;

    public Packet(String message, String action) {
        this.action=action;
        this.message = message;
    }

    public Packet(String message) {
        this.action="Generic Action";
        this.message = message;
    }
}
