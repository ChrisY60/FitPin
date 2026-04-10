package org.example.fitpinserver.business.services;

import org.example.fitpinserver.DAL.entities.UserEntity;
import org.example.fitpinserver.domain.models.User;

public interface UserService {
    public User registerUser(User user);
    public User loginUser(String usernameOrEmail, String password);
}
