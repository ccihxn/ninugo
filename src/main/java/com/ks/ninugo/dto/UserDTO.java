package com.ks.ninugo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {
    int id;
    String nickname;
    String loginId;
    String password;
    String email;
    int userState;
    String role;
    LocalDateTime createdAt;
}
