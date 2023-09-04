package com.ks.ninugo.dto;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
public class UserDTO {
    @NonNull
    int id;
    @NonNull
    String nickname;
    @NonNull
    String loginId;
    @NonNull
    String password;
    @NonNull
    String email;
    @NonNull
    int userState;
    @NonNull
    String role;
    @NonNull
    LocalDateTime createdAt;
}
