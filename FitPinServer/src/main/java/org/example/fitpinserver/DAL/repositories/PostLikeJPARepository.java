package org.example.fitpinserver.DAL.repositories;

import org.example.fitpinserver.DAL.entities.PostLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PostLikeJPARepository extends JpaRepository<PostLikeEntity, Long> {
    boolean existsByUser_IdAndPost_Id(Long userId, Long postId);
    Optional<PostLikeEntity> findByUser_IdAndPost_Id(Long userId, Long postId);
    List<PostLikeEntity> findByUser_UsernameAndPost_IdIn(String username, Collection<Long> postIds);
}
