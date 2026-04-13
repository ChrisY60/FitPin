package org.example.fitpinserver.domain.repositories;

import org.example.fitpinserver.domain.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmailAddress(String emailAddress);
    List<User> findAll();
    void deleteById(Long id);
    boolean existsByUsername(String username);
    boolean existsByEmailAddress(String emailAddress);
}
