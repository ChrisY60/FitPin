package org.example.fitpinserver.presentation.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class NotificationResponseDTO {
    private Long id;
    private String type;
    private Instant timestamp;
    private boolean read;
    private String actorUsername;
    private String actorProfilePictureUrl;
    private Long postId;
    private String postImageUrl;
}
