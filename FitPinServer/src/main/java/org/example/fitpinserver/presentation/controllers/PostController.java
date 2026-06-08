package org.example.fitpinserver.presentation.controllers;

import org.example.fitpinserver.business.services.CommentService;
import org.example.fitpinserver.business.services.PostLikeService;
import org.example.fitpinserver.business.services.PostService;
import org.example.fitpinserver.domain.models.Comment;
import org.example.fitpinserver.domain.models.Post;
import org.example.fitpinserver.presentation.dtos.CommentResponseDTO;
import org.example.fitpinserver.presentation.dtos.CreateCommentRequestDTO;
import org.example.fitpinserver.presentation.dtos.CreatePostRequestDTO;
import org.example.fitpinserver.presentation.dtos.PostLikeResponseDTO;
import org.example.fitpinserver.presentation.dtos.PostResponseDTO;
import org.example.fitpinserver.presentation.mappers.PostPresentationMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final PostPresentationMapper postPresentationMapper;
    private final PostLikeService postLikeService;
    private final CommentService commentService;

    public PostController(PostService postService, PostPresentationMapper postPresentationMapper,
                           PostLikeService postLikeService, CommentService commentService) {
        this.postService = postService;
        this.postPresentationMapper = postPresentationMapper;
        this.postLikeService = postLikeService;
        this.commentService = commentService;
    }

    @GetMapping
    public List<PostResponseDTO> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        Set<Long> likedPostIds = getLikedPostIds(posts);
        return posts.stream()
                .map(post -> postPresentationMapper.toPostResponseDTO(post, likedPostIds.contains(post.getId())))
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
        return postPresentationMapper.toPostResponseDTO(post, false);
    }

    @PostMapping("/{postId}/likes")
    @ResponseStatus(HttpStatus.CREATED)
    public PostLikeResponseDTO likePost(@PathVariable Long postId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int likeCount = postLikeService.likePost(username, postId);
        return new PostLikeResponseDTO(true, likeCount);
    }

    @DeleteMapping("/{postId}/likes")
    public PostLikeResponseDTO unlikePost(@PathVariable Long postId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int likeCount = postLikeService.unlikePost(username, postId);
        return new PostLikeResponseDTO(false, likeCount);
    }

    @GetMapping("/{postId}/comments")
    public List<CommentResponseDTO> getComments(@PathVariable Long postId) {
        return commentService.getCommentsByPostId(postId).stream()
                .map(this::toCommentResponseDTO)
                .toList();
    }

    @PostMapping("/{postId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDTO addComment(@PathVariable Long postId, @RequestBody CreateCommentRequestDTO requestDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Comment comment = commentService.addComment(username, postId, requestDTO.getContent());
        return toCommentResponseDTO(comment);
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        commentService.deleteComment(username, commentId);
    }

    private CommentResponseDTO toCommentResponseDTO(Comment comment) {
        return new CommentResponseDTO(
                comment.getId(),
                comment.getContent(),
                comment.getTimestamp(),
                comment.getAuthor().getUsername(),
                comment.getAuthor().getProfilePictureUrl()
        );
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
