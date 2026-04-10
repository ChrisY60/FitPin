package org.example.fitpinserver.presentation.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.example.fitpinserver.business.services.JwtService;
import org.example.fitpinserver.domain.models.User;
import org.example.fitpinserver.presentation.dtos.LoginUserRequestDTO;
import org.example.fitpinserver.presentation.dtos.LoginUserResponseDTO;
import org.example.fitpinserver.presentation.dtos.RegisterUserRequestDTO;
import org.example.fitpinserver.presentation.dtos.RegisterUserResponseDTO;
import org.example.fitpinserver.DAL.entities.UserEntity;
import org.example.fitpinserver.business.services.UserService;
import org.example.fitpinserver.presentation.mappers.UserPresentationMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
    private final UserService userService;
    private final UserPresentationMapper userPresentationMapper;
    private final long jwtExpirationMs;
    private final JwtService jwtService;

    public UserController(UserService userService, UserPresentationMapper userPresentationMapper, JwtService jwtService, @Value("${security.jwt.expiration-ms}") long jwtExpirationMs) {
        this.userService = userService;
        this.userPresentationMapper = userPresentationMapper;
        this.jwtService = jwtService;
        this.jwtExpirationMs = jwtExpirationMs;
    }

    @PostMapping("/register")
    public RegisterUserResponseDTO registerUser (@RequestBody RegisterUserRequestDTO requestDTO){
        User mappedUser = userPresentationMapper.toUserObjectFromRequestDTO(requestDTO);
        User savedUser = userService.registerUser(mappedUser);
        return userPresentationMapper.toRegisterUserResponseDTO(savedUser);
    }

    @PostMapping("/login")
    public LoginUserResponseDTO loginUser (@RequestBody LoginUserRequestDTO requestDTO, HttpServletResponse response){
        User loggedInUser = userService.loginUser(requestDTO.getEmailOrUsername(), requestDTO.getPassword());
        String token = jwtService.generateToken(loggedInUser);
        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofMillis(jwtExpirationMs))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return userPresentationMapper.toLoginUserResponseDTO(loggedInUser, token);
    }
}
