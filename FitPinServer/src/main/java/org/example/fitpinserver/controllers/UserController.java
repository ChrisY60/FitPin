package org.example.fitpinserver.controllers;

import org.example.fitpinserver.dtos.RegisterUserRequestDTO;
import org.example.fitpinserver.dtos.RegisterUserResponseDTO;
import org.example.fitpinserver.entities.User;
import org.example.fitpinserver.mappers.UserMapper;
import org.example.fitpinserver.services.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    public RegisterUserResponseDTO registerUser (@RequestBody RegisterUserRequestDTO requestDTO){
        User mappedUser = userMapper.toUserObjectFromRequestDTO(requestDTO);
        User savedUser = userService.registerUser(mappedUser);
        return userMapper.toRegisterUserResponseDTO(savedUser);
    }
}
