package org.example.fitpinserver.presentation.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@AllArgsConstructor
public class PostResponseDTO {
    private Long id;
    private String publisherUsername;
    private Instant timestamp;
    private String imageUrl;
    private String caption;
    private int likeCount;
    private int commentCount;
    private List<TagDTO> tags;
    private List<ProductDTO> products;
}
