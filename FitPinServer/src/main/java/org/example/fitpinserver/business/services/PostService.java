package org.example.fitpinserver.business.services;

import org.example.fitpinserver.domain.models.Post;

import java.util.List;

public interface PostService {
    List<Post> getAllPosts();
    List<Post> getPostsByUsername(String username);
    Post createPost(String publisherUsername, String caption, String imageUrl, List<Long> productIds, List<String> tagNames);
    void deletePost(String username, Long postId);
    List<Post> getPostsByProductId(Long productId);
    List<Post> getPostsByBrandId(Long brandId);
    List<Post> getPostsByTagId(Long tagId);
}
