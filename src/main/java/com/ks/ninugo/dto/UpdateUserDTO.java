package com.ks.ninugo.dto;

import lombok.Data;

@Data
public class UpdateUserDTO {
    private int id;
    private String nickname;
    private String password;
}
