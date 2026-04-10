package org.example.fitpinserver.presentation.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginUserResponseDTO {
    private final String token;
    private final Long userId;
    private final String username;
}
