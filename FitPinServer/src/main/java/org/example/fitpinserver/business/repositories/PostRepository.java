package org.example.fitpinserver.business.repositories;

import org.example.fitpinserver.domain.models.Post;

import java.util.List;

public interface PostRepository {
    List<Post> findAll();
}
