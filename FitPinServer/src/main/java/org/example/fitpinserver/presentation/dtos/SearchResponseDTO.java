package org.example.fitpinserver.presentation.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SearchResponseDTO {
    private List<ProductDTO> products;
    private List<BrandDTO> brands;
    private List<SearchUserDTO> users;
    private List<TagDTO> tags;
}
