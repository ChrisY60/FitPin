package org.example.fitpinserver.DAL.repositories;

import org.example.fitpinserver.DAL.entities.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostJPARepository extends JpaRepository<PostEntity, Long> {
}
