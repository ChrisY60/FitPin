package org.example.fitpinserver.presentation.dtos;

import lombok.Getter;

@Getter
public class UpdateProfileRequestDTO {
    private String bio;
    private String profilePictureUrl;
}
