package org.example.fitpinserver.DAL.repositories;

import org.example.fitpinserver.DAL.entities.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentJPARepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByPost_IdOrderByTimestampAsc(Long postId);
}
