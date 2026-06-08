package org.example.fitpinserver.business.repositories;

import org.example.fitpinserver.domain.models.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    List<Post> findAll();
    List<Post> findByPublisherUsername(String username);
    Optional<Post> findById(Long id);
    Post save(Post post);
}
