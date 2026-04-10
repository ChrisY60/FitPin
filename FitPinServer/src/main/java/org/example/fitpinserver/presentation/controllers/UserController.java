package org.example.fitpinserver.presentation.controllers;

import org.example.fitpinserver.business.services.JwtService;
import org.example.fitpinserver.domain.models.User;
import org.example.fitpinserver.presentation.dtos.LoginUserRequestDTO;
import org.example.fitpinserver.presentation.dtos.LoginUserResponseDTO;
import org.example.fitpinserver.presentation.dtos.RegisterUserRequestDTO;
import org.example.fitpinserver.presentation.dtos.RegisterUserResponseDTO;
import org.example.fitpinserver.DAL.entities.UserEntity;
import org.example.fitpinserver.business.services.UserService;
import org.example.fitpinserver.presentation.mappers.UserPresentationMapper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
    private final UserService userService;
    private final UserPresentationMapper userPresentationMapper;
    private final JwtService jwtService;

    public UserController(UserService userService, UserPresentationMapper userPresentationMapper, JwtService jwtService) {
        this.userService = userService;
        this.userPresentationMapper = userPresentationMapper;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public RegisterUserResponseDTO registerUser (@RequestBody RegisterUserRequestDTO requestDTO){
        User mappedUser = userPresentationMapper.toUserObjectFromRequestDTO(requestDTO);
        User savedUser = userService.registerUser(mappedUser);
        return userPresentationMapper.toRegisterUserResponseDTO(savedUser);
    }

    @PostMapping("/login")
    public LoginUserResponseDTO loginUser (@RequestBody LoginUserRequestDTO requestDTO){
        User loggedInUser = userService.loginUser(requestDTO.getEmailOrUsername(), requestDTO.getPassword());
        String token = jwtService.generateToken(loggedInUser);
        return userPresentationMapper.toLoginUserResponseDTO(loggedInUser, token);
    }
}
