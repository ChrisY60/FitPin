package org.example.fitpinserver.businessLayer.mappers;

import org.example.fitpinserver.presentationLayer.dtos.RegisterUserRequestDTO;
import org.example.fitpinserver.presentationLayer.dtos.RegisterUserResponseDTO;
import org.example.fitpinserver.domainLayer.entities.User;
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
