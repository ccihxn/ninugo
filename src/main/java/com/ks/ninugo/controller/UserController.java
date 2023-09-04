package com.ks.ninugo.controller;

import com.ks.ninugo.dto.UserDTO;
import com.ks.ninugo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    @GetMapping("/get")
    public void getUser() {
        userService.findUserById(6);
    }
    @PostMapping("/create")
    public void createUser() {
        userService.insertUser(new UserDTO("test", "test", "test", "test", "user"));
    }
}
