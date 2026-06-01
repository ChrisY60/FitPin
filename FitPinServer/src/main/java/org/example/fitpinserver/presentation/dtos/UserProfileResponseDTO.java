package org.example.fitpinserver.presentation.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserProfileResponseDTO {
    private Long id;
    private String username;
    private String bio;
    private String profilePictureUrl;
    private int postCount;
}
