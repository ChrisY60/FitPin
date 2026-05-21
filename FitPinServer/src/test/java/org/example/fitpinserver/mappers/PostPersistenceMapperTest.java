package org.example.fitpinserver.mappers;

import org.example.fitpinserver.DAL.entities.*;
import org.example.fitpinserver.DAL.mappers.PostPersistenceMapper;
import org.example.fitpinserver.domain.models.Post;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PostPersistenceMapperTest {

    private final PostPersistenceMapper mapper = new PostPersistenceMapper();

    @Test
    void toDomain_mapsAllFields() {
        UserEntity publisher = new UserEntity("alice", "alice@example.com", null, "hash");
        ReflectionTestUtils.setField(publisher, "id", 1L);

        Instant now = Instant.now();
        PostEntity postEntity = new PostEntity("caption text", publisher, now);
        ReflectionTestUtils.setField(postEntity, "id", 10L);
        postEntity.setImageUrl("https://example.com/img.jpg");

        TagEntity tag = new TagEntity();
        tag.setName("fashion");
        ReflectionTestUtils.setField(tag, "id", 5L);
        postEntity.getTags().add(tag);

        BrandEntity brand = new BrandEntity("Nike");
        ReflectionTestUtils.setField(brand, "id", 2L);
        ProductEntity product = new ProductEntity("Air Force 1", brand);
        ReflectionTestUtils.setField(product, "id", 3L);
        postEntity.getProducts().add(product);

        postEntity.getPostLikes().add(mock(PostLikeEntity.class));
        postEntity.getPostLikes().add(mock(PostLikeEntity.class));
        postEntity.getComments().add(mock(CommentEntity.class));

        Post result = mapper.toDomain(postEntity);

        assertEquals(10L, result.getId());
        assertEquals("caption text", result.getCaption());
        assertEquals("https://example.com/img.jpg", result.getImageUrl());
        assertEquals(now, result.getTimestamp());

        assertEquals(1L, result.getPublisher().getId());
        assertEquals("alice", result.getPublisher().getUsername());
        assertEquals("alice@example.com", result.getPublisher().getEmailAddress());

        assertEquals(1, result.getTags().size());
        assertEquals(5L, result.getTags().get(0).getId());
        assertEquals("fashion", result.getTags().get(0).getName());

        assertEquals(1, result.getProducts().size());
        assertEquals(3L, result.getProducts().get(0).getId());
        assertEquals("Air Force 1", result.getProducts().get(0).getName());
        assertEquals(2L, result.getProducts().get(0).getBrand().getId());
        assertEquals("Nike", result.getProducts().get(0).getBrand().getName());

        assertEquals(2, result.getPostLikes().size());
        assertEquals(1, result.getComments().size());
    }

    @Test
    void toDomain_nullEntity_returnsNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    void toDomain_emptyCollections_mapsToEmptyLists() {
        UserEntity publisher = new UserEntity("bob", "bob@example.com", null, "hash");
        ReflectionTestUtils.setField(publisher, "id", 2L);

        PostEntity postEntity = new PostEntity("caption", publisher, Instant.now());
        ReflectionTestUtils.setField(postEntity, "id", 20L);
        postEntity.setImageUrl("https://example.com/img.jpg");

        Post result = mapper.toDomain(postEntity);

        assertTrue(result.getTags().isEmpty());
        assertTrue(result.getProducts().isEmpty());
        assertTrue(result.getPostLikes().isEmpty());
        assertTrue(result.getComments().isEmpty());
    }
}
