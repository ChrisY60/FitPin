package org.example.fitpinserver.presentation.controllers;

import org.example.fitpinserver.business.services.PostService;
import org.example.fitpinserver.presentation.dtos.PostResponseDTO;
import org.example.fitpinserver.presentation.mappers.PostPresentationMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@CrossOrigin(origins = "http://localhost:5173")
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
}
