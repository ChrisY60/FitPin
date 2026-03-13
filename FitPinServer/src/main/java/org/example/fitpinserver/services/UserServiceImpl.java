package org.example.fitpinserver.services;

import org.example.fitpinserver.dtos.RegisterUserRequestDTO;
import org.example.fitpinserver.dtos.RegisterUserResponseDTO;
import org.example.fitpinserver.entities.User;
import org.example.fitpinserver.mappers.UserMapper;
import org.example.fitpinserver.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }


    @Override
    public User registerUser(User user) {
        if(userRepository.existsByUsername(user.getUsername()))
            throw new RuntimeException("Username is already in use");

        if(userRepository.existsByEmailAddress(user.getEmailAddress()))
            throw new RuntimeException("Email is already in use");

        //I will add password hashing later for now its plain text

        return userRepository.save(user);
    }
}
