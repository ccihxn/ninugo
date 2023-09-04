package com.ks.ninugo.service;

import com.ks.ninugo.dto.UserDTO;
import com.ks.ninugo.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    UserMapper userMapper;
    public void insertUser(UserDTO userDTO) {
        userMapper.insertUser(userDTO);
    }
}
