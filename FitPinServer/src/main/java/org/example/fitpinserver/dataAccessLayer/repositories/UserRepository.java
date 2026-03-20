package org.example.fitpinserver.dataAccessLayer.repositories;

import org.example.fitpinserver.domainLayer.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmailAddress(String emailAddress);
    Optional<User> findByEmailAddress(String emailAddress);
    Optional<User> findByUsername(String username);
}
