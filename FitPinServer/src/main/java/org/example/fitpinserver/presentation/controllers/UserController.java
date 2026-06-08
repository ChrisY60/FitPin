package org.example.fitpinserver.presentation.controllers;

import org.example.fitpinserver.business.services.PostLikeService;
import org.example.fitpinserver.business.services.PostService;
import org.example.fitpinserver.business.services.UserService;
import org.example.fitpinserver.domain.models.Post;
import org.example.fitpinserver.domain.models.User;
import org.example.fitpinserver.presentation.dtos.PostResponseDTO;
import org.example.fitpinserver.presentation.dtos.UpdateProfileRequestDTO;
import org.example.fitpinserver.presentation.dtos.UserProfileResponseDTO;
import org.example.fitpinserver.presentation.mappers.PostPresentationMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final PostService postService;
    private final PostPresentationMapper postPresentationMapper;
    private final PostLikeService postLikeService;

    public UserController(UserService userService, PostService postService, PostPresentationMapper postPresentationMapper,
                           PostLikeService postLikeService) {
        this.userService = userService;
        this.postService = postService;
        this.postPresentationMapper = postPresentationMapper;
        this.postLikeService = postLikeService;
    }

    @GetMapping("/{username}")
    public UserProfileResponseDTO getUserProfile(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        int postCount = postService.getPostsByUsername(username).size();
        return new UserProfileResponseDTO(user.getId(), user.getUsername(), user.getBio(), user.getProfilePictureUrl(), postCount);
    }

    @GetMapping("/{username}/posts")
    public List<PostResponseDTO> getUserPosts(@PathVariable String username) {
        List<Post> posts = postService.getPostsByUsername(username);
        Set<Long> likedPostIds = getLikedPostIds(posts);
        return posts.stream()
                .map(post -> postPresentationMapper.toPostResponseDTO(post, likedPostIds.contains(post.getId())))
                .toList();
    }

    @PutMapping("/me")
    public UserProfileResponseDTO updateOwnProfile(@RequestBody UpdateProfileRequestDTO requestDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.updateProfile(username, requestDTO.getBio(), requestDTO.getProfilePictureUrl());
        int postCount = postService.getPostsByUsername(username).size();
        return new UserProfileResponseDTO(user.getId(), user.getUsername(), user.getBio(), user.getProfilePictureUrl(), postCount);
    }

    private Set<Long> getLikedPostIds(List<Post> posts) {
        String username = getCurrentUsername();
        if (username == null || posts.isEmpty()) {
            return Set.of();
        }
        return postLikeService.getLikedPostIds(username, posts.stream().map(Post::getId).toList());
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
            return null;
        }
        return authentication.getName();
    }
}
