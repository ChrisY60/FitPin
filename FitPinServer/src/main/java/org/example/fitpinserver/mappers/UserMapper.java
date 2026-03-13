package org.example.fitpinserver.mappers;

import org.example.fitpinserver.dtos.RegisterUserRequestDTO;
import org.example.fitpinserver.dtos.RegisterUserResponseDTO;
import org.example.fitpinserver.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public RegisterUserResponseDTO toRegisterUserResponseDTO (User user){
        return new RegisterUserResponseDTO(user.getId(), user.getUsername(), user.getEmailAddress());
    }
    public User toUserObjectFromRequestDTO(RegisterUserRequestDTO requestDTO) {
        return new User(
                requestDTO.getUsername(),
                requestDTO.getEmailAddress(),
                null,
                requestDTO.getPassword()
        );
    }
}
