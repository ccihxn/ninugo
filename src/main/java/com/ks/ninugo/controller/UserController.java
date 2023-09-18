package com.ks.ninugo.controller;

import com.ks.ninugo.configure.security.provider.JwtTokenProvider;
import com.ks.ninugo.dto.UserDTO;
import com.ks.ninugo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    @GetMapping("/get")
    public void getUser() {
        String token = jwtTokenProvider.createToken("test", "user");
        System.out.println(token);
        System.out.println(jwtTokenProvider.getUsername(token));
        System.out.println(jwtTokenProvider.validationToken(token));
    }
    @PostMapping("/create")
    public void createUser() {
        userService.insertUser(new UserDTO("test", "test", "test", "test", "user"));
    }
}
