package org.example.fitpinserver.business.services;

import org.example.fitpinserver.domain.models.Post;

import java.util.List;

public interface PostService {
    List<Post> getAllPosts();
    List<Post> getPostsByUsername(String username);
    Post createPost(String publisherUsername, String caption, String imageUrl, List<Long> productIds, List<String> tagNames);
}
