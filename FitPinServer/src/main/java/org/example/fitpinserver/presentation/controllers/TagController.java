package org.example.fitpinserver.presentation.controllers;

import org.example.fitpinserver.business.services.PostLikeService;
import org.example.fitpinserver.business.services.PostService;
import org.example.fitpinserver.domain.models.Post;
import org.example.fitpinserver.presentation.dtos.PostResponseDTO;
import org.example.fitpinserver.presentation.mappers.PostPresentationMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final PostService postService;
    private final PostPresentationMapper postPresentationMapper;
    private final PostLikeService postLikeService;

    public TagController(PostService postService, PostPresentationMapper postPresentationMapper,
                         PostLikeService postLikeService) {
        this.postService = postService;
        this.postPresentationMapper = postPresentationMapper;
        this.postLikeService = postLikeService;
    }

    @GetMapping("/{tagId}/posts")
    public List<PostResponseDTO> getPostsByTag(@PathVariable Long tagId) {
        List<Post> posts = postService.getPostsByTagId(tagId);
        Set<Long> likedPostIds = getLikedPostIds(posts);
        return posts.stream()
                .map(post -> postPresentationMapper.toPostResponseDTO(post, likedPostIds.contains(post.getId())))
                .toList();
    }

    private Set<Long> getLikedPostIds(List<Post> posts) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName()) || posts.isEmpty()) {
            return Set.of();
        }
        return postLikeService.getLikedPostIds(auth.getName(), posts.stream().map(Post::getId).toList());
    }
}
