package com.ks.ninugo.dto;

import java.time.LocalDateTime;

public class ChatDTO {
    private int id;
    private int user1Id;
    private int user1State;
    private int user2Id;
    private int user2State;
    private LocalDateTime matchingTime;
    private String lastChat;
}
