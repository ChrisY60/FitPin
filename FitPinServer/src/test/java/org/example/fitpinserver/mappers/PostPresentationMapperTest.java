package org.example.fitpinserver.mappers;

import org.example.fitpinserver.domain.models.*;
import org.example.fitpinserver.presentation.dtos.PostResponseDTO;
import org.example.fitpinserver.presentation.mappers.PostPresentationMapper;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class PostPresentationMapperTest {

    private final PostPresentationMapper mapper = new PostPresentationMapper();

    @Test
    void toPostResponseDTO_mapsAllFields() {
        User publisher = new User(1L, "alice", "alice@example.com", null, "hashed");
        Instant now = Instant.now();
        Post post = new Post(10L, "nice outfit", now, publisher);
        post.setImageUrl("https://example.com/img.jpg");

        post.getTags().add(new Tag(1L, "streetwear"));
        post.getTags().add(new Tag(2L, "summer"));

        Brand brand = new Brand(1L, "Nike");
        post.getProducts().add(new Product(1L, "Air Force 1", brand));

        post.getPostLikes().add(new PostLike());
        post.getPostLikes().add(new PostLike());
        post.getPostLikes().add(new PostLike());

        post.getComments().add(new Comment());
        post.getComments().add(new Comment());

        PostResponseDTO result = mapper.toPostResponseDTO(post);

        assertEquals(10L, result.getId());
        assertEquals("alice", result.getPublisherUsername());
        assertEquals(now, result.getTimestamp());
        assertEquals("https://example.com/img.jpg", result.getImageUrl());
        assertEquals("nice outfit", result.getCaption());
        assertEquals(3, result.getLikeCount());
        assertEquals(2, result.getCommentCount());

        assertEquals(2, result.getTags().size());
        assertEquals(1L, result.getTags().get(0).getId());
        assertEquals("streetwear", result.getTags().get(0).getName());
        assertEquals(2L, result.getTags().get(1).getId());
        assertEquals("summer", result.getTags().get(1).getName());

        assertEquals(1, result.getProducts().size());
        assertEquals(1L, result.getProducts().get(0).getId());
        assertEquals("Air Force 1", result.getProducts().get(0).getName());
        assertEquals(1L, result.getProducts().get(0).getBrand().getId());
        assertEquals("Nike", result.getProducts().get(0).getBrand().getName());
    }

    @Test
    void toPostResponseDTO_emptyCollections_mapsToEmptyLists() {
        User publisher = new User(1L, "alice", "alice@example.com", null, "hashed");
        Post post = new Post(5L, "caption", Instant.now(), publisher);
        post.setImageUrl("https://example.com/img.jpg");

        PostResponseDTO result = mapper.toPostResponseDTO(post);

        assertEquals(0, result.getLikeCount());
        assertEquals(0, result.getCommentCount());
        assertTrue(result.getTags().isEmpty());
        assertTrue(result.getProducts().isEmpty());
    }
}
