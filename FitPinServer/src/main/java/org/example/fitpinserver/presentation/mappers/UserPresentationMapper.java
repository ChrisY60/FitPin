package org.example.fitpinserver.presentation.mappers;

import org.example.fitpinserver.domain.models.User;
import org.example.fitpinserver.presentation.dtos.LoginUserResponseDTO;
import org.example.fitpinserver.presentation.dtos.RegisterUserRequestDTO;
import org.example.fitpinserver.presentation.dtos.RegisterUserResponseDTO;
import org.example.fitpinserver.DAL.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserPresentationMapper {
    public RegisterUserResponseDTO toRegisterUserResponseDTO (User user){
        return new RegisterUserResponseDTO(user.getId(), user.getUsername(), user.getEmailAddress());
    }

    public LoginUserResponseDTO toLoginUserResponseDTO(User user, String token) {
        return new LoginUserResponseDTO(
                token,
                user.getId(),
                user.getUsername()
        );
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
