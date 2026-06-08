package org.example.fitpinserver.business.repositories;

import org.example.fitpinserver.domain.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    List<Comment> findByPostId(Long postId);
    Comment save(Comment comment);
    Optional<Comment> findById(Long id);
    void deleteById(Long id);
}
