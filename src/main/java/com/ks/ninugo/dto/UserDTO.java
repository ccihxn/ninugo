package com.ks.ninugo.dto;


import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
public class UserDTO {

    private int id;
    @NonNull
    private String nickname;
    @NonNull
    private String loginId;
    @NonNull
    private String password;
    @NonNull
    private String email;
    private int userState;
    @NonNull
    private String role;
    @NonNull
    private LocalDateTime createdAt;
}
