package org.example.fitpinserver.business.serviceImplementations;

import org.example.fitpinserver.business.repositories.PostLikeRepository;
import org.example.fitpinserver.business.repositories.PostRepository;
import org.example.fitpinserver.business.repositories.UserRepository;
import org.example.fitpinserver.business.services.PostLikeService;
import org.example.fitpinserver.domain.models.Post;
import org.example.fitpinserver.domain.models.PostLike;
import org.example.fitpinserver.domain.models.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostLikeServiceImpl(PostLikeRepository postLikeRepository, PostRepository postRepository, UserRepository userRepository) {
        this.postLikeRepository = postLikeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public int likePost(String username, Long postId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (postLikeRepository.existsByUserIdAndPostId(user.getId(), post.getId())) {
            throw new RuntimeException("Post already liked");
        }

        postLikeRepository.save(new PostLike(Instant.now(), user, post));
        return post.getPostLikes().size() + 1;
    }

    @Override
    @Transactional
    public int unlikePost(String username, Long postId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!postLikeRepository.existsByUserIdAndPostId(user.getId(), post.getId())) {
            throw new RuntimeException("Post not liked");
        }

        postLikeRepository.deleteByUserIdAndPostId(user.getId(), postId);
        return Math.max(post.getPostLikes().size() - 1, 0);
    }

    @Override
    public Set<Long> getLikedPostIds(String username, List<Long> postIds) {
        return postLikeRepository.findLikedPostIds(username, postIds);
    }
}
