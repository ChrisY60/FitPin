package org.example.fitpinserver.DAL.repositories;

import org.example.fitpinserver.DAL.entities.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagJPARepository extends JpaRepository<TagEntity, Long> {
    Optional<TagEntity> findByName(String name);
}
