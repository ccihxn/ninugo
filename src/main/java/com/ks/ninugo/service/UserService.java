package com.ks.ninugo.service;

import com.ks.ninugo.dto.UpdateUserDTO;
import com.ks.ninugo.dto.UserDTO;
import com.ks.ninugo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    public Optional<UserDTO> findUserByLoginId(String loginId) {
        UserDTO userDTO = userMapper.findUserByLoginId(loginId);
        return Optional.ofNullable(userDTO);
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
}