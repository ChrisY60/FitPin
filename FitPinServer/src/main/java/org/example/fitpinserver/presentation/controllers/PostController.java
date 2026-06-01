package org.example.fitpinserver.presentation.controllers;

import org.example.fitpinserver.business.services.PostService;
import org.example.fitpinserver.domain.models.Post;
import org.example.fitpinserver.presentation.dtos.CreatePostRequestDTO;
import org.example.fitpinserver.presentation.dtos.PostResponseDTO;
import org.example.fitpinserver.presentation.mappers.PostPresentationMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final PostPresentationMapper postPresentationMapper;

    public PostController(PostService postService, PostPresentationMapper postPresentationMapper) {
        this.postService = postService;
        this.postPresentationMapper = postPresentationMapper;
    }

    @GetMapping
    public List<PostResponseDTO> getAllPosts() {
        return postService.getAllPosts().stream()
                .map(postPresentationMapper::toPostResponseDTO)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponseDTO createPost(@RequestBody CreatePostRequestDTO requestDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Post post = postService.createPost(
                username,
                requestDTO.getCaption(),
                requestDTO.getImageUrl(),
                requestDTO.getProductIds(),
                requestDTO.getTagNames()
        );
        return postPresentationMapper.toPostResponseDTO(post);
    }
}
