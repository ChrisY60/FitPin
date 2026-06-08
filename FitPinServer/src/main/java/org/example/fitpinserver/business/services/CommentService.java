package org.example.fitpinserver.business.services;

import org.example.fitpinserver.domain.models.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getCommentsByPostId(Long postId);
    Comment addComment(String username, Long postId, String content);
    void deleteComment(String username, Long commentId);
}
