package org.example.fitpinserver.presentationLayer.controllers;

import org.example.fitpinserver.presentationLayer.dtos.RegisterUserRequestDTO;
import org.example.fitpinserver.presentationLayer.dtos.RegisterUserResponseDTO;
import org.example.fitpinserver.domainLayer.entities.User;
import org.example.fitpinserver.businessLayer.mappers.UserMapper;
import org.example.fitpinserver.businessLayer.services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173")
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
