package org.example.fitpinserver.DAL.repositories;

import org.example.fitpinserver.DAL.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJPARepository extends JpaRepository<UserEntity, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmailAddress(String emailAddress);
    Optional<UserEntity> findById(Long id);
    Optional<UserEntity> findByEmailAddress(String emailAddress);
    Optional<UserEntity> findByUsername(String username);
}
