package org.example.fitpinserver.presentation.controllers;

import org.example.fitpinserver.business.services.PostService;
import org.example.fitpinserver.business.services.UserService;
import org.example.fitpinserver.domain.models.User;
import org.example.fitpinserver.presentation.dtos.PostResponseDTO;
import org.example.fitpinserver.presentation.dtos.UpdateProfileRequestDTO;
import org.example.fitpinserver.presentation.dtos.UserProfileResponseDTO;
import org.example.fitpinserver.presentation.mappers.PostPresentationMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final PostService postService;
    private final PostPresentationMapper postPresentationMapper;

    public UserController(UserService userService, PostService postService, PostPresentationMapper postPresentationMapper) {
        this.userService = userService;
        this.postService = postService;
        this.postPresentationMapper = postPresentationMapper;
    }

    @GetMapping("/{username}")
    public UserProfileResponseDTO getUserProfile(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        int postCount = postService.getPostsByUsername(username).size();
        return new UserProfileResponseDTO(user.getId(), user.getUsername(), user.getBio(), user.getProfilePictureUrl(), postCount);
    }

    @GetMapping("/{username}/posts")
    public List<PostResponseDTO> getUserPosts(@PathVariable String username) {
        return postService.getPostsByUsername(username).stream()
                .map(postPresentationMapper::toPostResponseDTO)
                .toList();
    }

    @PutMapping("/me")
    public UserProfileResponseDTO updateOwnProfile(@RequestBody UpdateProfileRequestDTO requestDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.updateProfile(username, requestDTO.getBio(), requestDTO.getProfilePictureUrl());
        int postCount = postService.getPostsByUsername(username).size();
        return new UserProfileResponseDTO(user.getId(), user.getUsername(), user.getBio(), user.getProfilePictureUrl(), postCount);
    }
}
