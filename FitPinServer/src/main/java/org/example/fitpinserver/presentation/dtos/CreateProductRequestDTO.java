package org.example.fitpinserver.presentation.dtos;

import lombok.Getter;

@Getter
public class CreateProductRequestDTO {
    private String name;
    private Long brandId;
    private String imageUrl;
}
