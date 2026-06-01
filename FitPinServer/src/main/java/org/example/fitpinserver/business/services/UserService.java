package org.example.fitpinserver.business.services;

import org.example.fitpinserver.DAL.entities.UserEntity;
import org.example.fitpinserver.domain.models.User;

public interface UserService {
    User registerUser(User user);
    User loginUser(String usernameOrEmail, String password);
    User getUserByUsername(String username);
    User updateProfile(String username, String bio, String profilePictureUrl);
}
