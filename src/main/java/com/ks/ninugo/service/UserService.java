package com.ks.ninugo.service;

import com.ks.ninugo.dto.ChatDTO;
import com.ks.ninugo.dto.UpdateUserDTO;
import com.ks.ninugo.dto.UserDTO;
import com.ks.ninugo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public void insertUser(UserDTO userDTO) {
        userMapper.insertUser(userDTO);
        System.out.println(userDTO);
    }

    public UserDTO findUserById(int userId) {
        return userMapper.findUserById(userId);
    }

    public void deleteUserById(int userId) {
        userMapper.deleteUserById(userId);
    }

    public void updateUser(UpdateUserDTO updateUserDTO) {
        userMapper.updateUser(updateUserDTO);
    }

    public void insertQueue(int userId) {
        userMapper.insertQueue(userId);
    }

    public void matchedQueue() {
        userMapper.matchedQueue();
    }

    public void deleteQueue() {
        userMapper.deleteQueue();
    }

    public void createChat(ChatDTO chatDTO) {
        userMapper.createChat(chatDTO);
    }

    public void updateLastChat(int chatId) {
        userMapper.updateLastChat(chatId);
    }
}