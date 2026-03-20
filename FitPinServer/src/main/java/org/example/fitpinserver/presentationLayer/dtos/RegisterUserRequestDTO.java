package org.example.fitpinserver.presentationLayer.dtos;

import lombok.Getter;
import lombok.Setter;

public class RegisterUserRequestDTO {
    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String emailAddress;

    @Getter
    @Setter
    private String password;

    public RegisterUserRequestDTO() {
    }

    public RegisterUserRequestDTO(String username, String emailAddress, String password) {
        this.username = username;
        this.emailAddress = emailAddress;
        this.password = password;
    }
}
