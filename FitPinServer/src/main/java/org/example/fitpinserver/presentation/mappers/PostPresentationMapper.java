package org.example.fitpinserver.presentation.mappers;

import org.example.fitpinserver.domain.models.Post;
import org.example.fitpinserver.presentation.dtos.BrandDTO;
import org.example.fitpinserver.presentation.dtos.PostResponseDTO;
import org.example.fitpinserver.presentation.dtos.ProductDTO;
import org.example.fitpinserver.presentation.dtos.TagDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostPresentationMapper {

    public PostResponseDTO toPostResponseDTO(Post post) {
        List<TagDTO> tags = post.getTags().stream()
                .map(tag -> new TagDTO(tag.getId(), tag.getName()))
                .toList();

        List<ProductDTO> products = post.getProducts().stream()
                .map(product -> new ProductDTO(
                        product.getId(),
                        product.getName(),
                        new BrandDTO(product.getBrand().getId(), product.getBrand().getName())
                ))
                .toList();

        return new PostResponseDTO(
                post.getId(),
                post.getPublisher().getUsername(),
                post.getTimestamp(),
                post.getImageUrl(),
                post.getCaption(),
                post.getPostLikes().size(),
                post.getComments().size(),
                tags,
                products
        );
    }
}
