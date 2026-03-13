package org.example.fitpinserver.dtos;

import lombok.Getter;
import lombok.Setter;

public class RegisterUserResponseDTO {
    @Getter
    private Long id;

    @Getter
    private String username;

    @Getter
    private String emailAddress;

    public RegisterUserResponseDTO() {
    }

    public RegisterUserResponseDTO(Long id, String username, String emailAddress) {
        this.id = id;
        this.username = username;
        this.emailAddress = emailAddress;
    }
}
