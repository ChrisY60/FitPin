package org.example.fitpinserver.services;

import org.example.fitpinserver.dtos.RegisterUserRequestDTO;
import org.example.fitpinserver.dtos.RegisterUserResponseDTO;
import org.example.fitpinserver.entities.User;
import org.example.fitpinserver.mappers.UserMapper;

public interface UserService {
    public User registerUser(User user);
}
