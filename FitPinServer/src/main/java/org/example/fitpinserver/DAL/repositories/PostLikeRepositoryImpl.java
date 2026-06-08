package org.example.fitpinserver.DAL.repositories;

import org.example.fitpinserver.DAL.entities.PostEntity;
import org.example.fitpinserver.DAL.entities.PostLikeEntity;
import org.example.fitpinserver.DAL.entities.UserEntity;
import org.example.fitpinserver.business.repositories.PostLikeRepository;
import org.example.fitpinserver.domain.models.PostLike;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class PostLikeRepositoryImpl implements PostLikeRepository {

    private final PostLikeJPARepository postLikeJPARepository;
    private final UserJPARepository userJPARepository;
    private final PostJPARepository postJPARepository;

    public PostLikeRepositoryImpl(PostLikeJPARepository postLikeJPARepository, UserJPARepository userJPARepository,
                                  PostJPARepository postJPARepository) {
        this.postLikeJPARepository = postLikeJPARepository;
        this.userJPARepository = userJPARepository;
        this.postJPARepository = postJPARepository;
    }

    @Override
    public boolean existsByUserIdAndPostId(Long userId, Long postId) {
        return postLikeJPARepository.existsByUser_IdAndPost_Id(userId, postId);
    }

    @Override
    public void save(PostLike postLike) {
        UserEntity userEntity = userJPARepository.findById(postLike.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        PostEntity postEntity = postJPARepository.findById(postLike.getPost().getId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        postLikeJPARepository.save(new PostLikeEntity(postLike.getTimestamp(), userEntity, postEntity));
    }

    @Override
    public void deleteByUserIdAndPostId(Long userId, Long postId) {
        PostEntity postEntity = postJPARepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        PostLikeEntity likeEntity = postLikeJPARepository.findByUser_IdAndPost_Id(userId, postId)
                .orElseThrow(() -> new RuntimeException("Like not found"));

        postEntity.removePostLike(likeEntity);
        postJPARepository.save(postEntity);
    }

    @Override
    public Set<Long> findLikedPostIds(String username, List<Long> postIds) {
        if (postIds.isEmpty()) {
            return Set.of();
        }

        return postLikeJPARepository.findByUser_UsernameAndPost_IdIn(username, postIds).stream()
                .map(like -> like.getPost().getId())
                .collect(Collectors.toSet());
    }
}
