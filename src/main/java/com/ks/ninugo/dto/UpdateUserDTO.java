package com.ks.ninugo.dto;

import lombok.Data;

@Data
public class UpdateUserDTO {
    int id;
    String nickname;
    String password;
}
