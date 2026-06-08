package org.example.fitpinserver.business.services;

import java.util.List;
import java.util.Set;

public interface PostLikeService {
    int likePost(String username, Long postId);
    int unlikePost(String username, Long postId);
    Set<Long> getLikedPostIds(String username, List<Long> postIds);
}
