package org.example.fitpinserver.presentation.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchUserDTO {
    private Long id;
    private String username;
    private String profilePictureUrl;
}
