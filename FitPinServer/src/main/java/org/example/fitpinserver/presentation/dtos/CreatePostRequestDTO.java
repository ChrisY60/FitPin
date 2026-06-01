package org.example.fitpinserver.presentation.dtos;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CreatePostRequestDTO {
    private String caption;
    private String imageUrl;
    private List<Long> productIds = new ArrayList<>();
    private List<String> tagNames = new ArrayList<>();
}
