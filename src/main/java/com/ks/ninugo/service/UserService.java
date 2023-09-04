package com.ks.ninugo.service;

import com.ks.ninugo.dto.UserDTO;
import com.ks.ninugo.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    UserMapper userMapper;
    private void insertUser(UserDTO userDTO) {
        userMapper.insertUser(userDTO);
    }
    private UserDTO findUserById(int userId) {
        return userMapper.findUserById(userId);
    }
    private void deleteUserById(int userId) {
        userMapper.deleteUserById(userId);
    }
}
