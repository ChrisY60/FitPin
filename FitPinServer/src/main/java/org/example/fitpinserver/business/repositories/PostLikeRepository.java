package org.example.fitpinserver.business.repositories;

import org.example.fitpinserver.domain.models.PostLike;

import java.util.List;
import java.util.Set;

public interface PostLikeRepository {
    boolean existsByUserIdAndPostId(Long userId, Long postId);
    void save(PostLike postLike);
    void deleteByUserIdAndPostId(Long userId, Long postId);
    Set<Long> findLikedPostIds(String username, List<Long> postIds);
}
