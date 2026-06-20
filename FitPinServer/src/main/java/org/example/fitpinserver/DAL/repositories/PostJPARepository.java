package org.example.fitpinserver.DAL.repositories;

import org.example.fitpinserver.DAL.entities.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostJPARepository extends JpaRepository<PostEntity, Long> {
    List<PostEntity> findByPublisher_Username(String username);
    List<PostEntity> findByProducts_Id(Long productId);
    List<PostEntity> findByProducts_Brand_Id(Long brandId);
    List<PostEntity> findByTags_Id(Long tagId);
}
