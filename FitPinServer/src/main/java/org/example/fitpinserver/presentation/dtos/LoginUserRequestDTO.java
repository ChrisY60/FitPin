package org.example.fitpinserver.presentation.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserRequestDTO {
    private String emailOrUsername;
    private String password;
}
