package com.ks.ninugo.dto;

import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

public class QueueDTO {
    private int id;
    private int user_id;
    private int matchingState;
    private LocalDateTime startTime;
    private LocalDateTime matchedTime;
}
