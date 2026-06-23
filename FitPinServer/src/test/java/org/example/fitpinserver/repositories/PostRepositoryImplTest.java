package org.example.fitpinserver.repositories;

import org.example.fitpinserver.DAL.entities.*;
import org.example.fitpinserver.DAL.mappers.PostPersistenceMapper;
import org.example.fitpinserver.DAL.repositories.*;
import org.example.fitpinserver.business.services.SearchIndexService;
import org.example.fitpinserver.domain.models.Brand;
import org.example.fitpinserver.domain.models.Post;
import org.example.fitpinserver.domain.models.Product;
import org.example.fitpinserver.domain.models.Tag;
import org.example.fitpinserver.domain.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostRepositoryImplTest {

    @Mock
    private PostJPARepository postJPARepository;

    @Mock
    private PostPersistenceMapper postPersistenceMapper;

    @Mock
    private UserJPARepository userJPARepository;

    @Mock
    private ProductJPARepository productJPARepository;

    @Mock
    private TagJPARepository tagJPARepository;

    @Mock
    private SearchIndexService searchIndexService;

    @InjectMocks
    private PostRepositoryImpl postRepository;

    private UserEntity makeUserEntity(Long id, String username) {
        UserEntity entity = new UserEntity(username, username + "@example.com", null, "hash");
        ReflectionTestUtils.setField(entity, "id", id);
        return entity;
    }

    @Test
    void findAll_Should_Return_All_Mapped_Posts() {
        User publisher = new User(1L, "chris", "chris@example.com", null, "hash");
        UserEntity publisherEntity = makeUserEntity(1L, "chris");
        Instant now = Instant.now();

        PostEntity e1 = new PostEntity("caption1", publisherEntity, now);
        ReflectionTestUtils.setField(e1, "id", 1L);
        PostEntity e2 = new PostEntity("caption2", publisherEntity, now);
        ReflectionTestUtils.setField(e2, "id", 2L);

        Post p1 = new Post(1L, "caption1", now, publisher);
        Post p2 = new Post(2L, "caption2", now, publisher);

        when(postJPARepository.findAll()).thenReturn(List.of(e1, e2));
        when(postPersistenceMapper.toDomain(e1)).thenReturn(p1);
        when(postPersistenceMapper.toDomain(e2)).thenReturn(p2);

        List<Post> result = postRepository.findAll();

        assertEquals(2, result.size());
        assertEquals(p1, result.get(0));
        assertEquals(p2, result.get(1));
    }

    @Test
    void findAll_Should_Return_Empty_List_When_No_Posts_Exist() {
        when(postJPARepository.findAll()).thenReturn(List.of());

        List<Post> result = postRepository.findAll();

        assertTrue(result.isEmpty());
    }

    @Test
    void findByPublisherUsername_Should_Return_Posts_For_User() {
        User publisher = new User(1L, "chris", "chris@example.com", null, "hash");
        UserEntity publisherEntity = makeUserEntity(1L, "chris");
        Instant now = Instant.now();

        PostEntity e1 = new PostEntity("caption1", publisherEntity, now);
        Post p1 = new Post(1L, "caption1", now, publisher);

        when(postJPARepository.findByPublisher_Username("chris")).thenReturn(List.of(e1));
        when(postPersistenceMapper.toDomain(e1)).thenReturn(p1);

        List<Post> result = postRepository.findByPublisherUsername("chris");

        assertEquals(1, result.size());
        assertEquals(p1, result.get(0));
    }

    @Test
    void findByPublisherUsername_Should_Return_Empty_List_When_User_Has_No_Posts() {
        when(postJPARepository.findByPublisher_Username("chris")).thenReturn(List.of());

        List<Post> result = postRepository.findByPublisherUsername("chris");

        assertTrue(result.isEmpty());
    }

    @Test
    void save_Should_Persist_Post_With_Existing_Products_And_Tags() {
        User publisher = new User(1L, "chris", "chris@example.com", null, "hash");
        Product product = new Product(2L, "Shirt", new Brand(1L, "Nike"));
        Post post = new Post("caption", Instant.now(), publisher);
        post.getProducts().add(product);
        post.getTags().add(new Tag(null, "fashion"));

        UserEntity publisherEntity = makeUserEntity(1L, "chris");
        PostEntity postEntity = new PostEntity("caption", publisherEntity, post.getTimestamp());

        BrandEntity brandEntity = new BrandEntity("Nike");
        ReflectionTestUtils.setField(brandEntity, "id", 1L);
        ProductEntity productEntity = new ProductEntity("Shirt", brandEntity);
        ReflectionTestUtils.setField(productEntity, "id", 2L);

        TagEntity tagEntity = new TagEntity();
        tagEntity.setName("fashion");
        ReflectionTestUtils.setField(tagEntity, "id", 5L);

        PostEntity savedEntity = new PostEntity("caption", publisherEntity, post.getTimestamp());
        ReflectionTestUtils.setField(savedEntity, "id", 10L);
        Post expectedPost = new Post(10L, "caption", post.getTimestamp(), publisher);

        when(userJPARepository.findById(1L)).thenReturn(Optional.of(publisherEntity));
        when(postPersistenceMapper.toEntity(post, publisherEntity)).thenReturn(postEntity);
        when(productJPARepository.findById(2L)).thenReturn(Optional.of(productEntity));
        when(tagJPARepository.findByName("fashion")).thenReturn(Optional.of(tagEntity));
        when(postJPARepository.save(postEntity)).thenReturn(savedEntity);
        when(postPersistenceMapper.toDomain(savedEntity)).thenReturn(expectedPost);

        Post result = postRepository.save(post);

        assertEquals(expectedPost, result);
        assertTrue(postEntity.getProducts().contains(productEntity));
        assertTrue(postEntity.getTags().contains(tagEntity));
        verify(postJPARepository).save(postEntity);
    }

    @Test
    void save_Should_Create_New_Tag_When_Tag_Does_Not_Exist() {
        User publisher = new User(1L, "chris", "chris@example.com", null, "hash");
        Post post = new Post("caption", Instant.now(), publisher);
        post.getTags().add(new Tag(null, "newTag"));

        UserEntity publisherEntity = makeUserEntity(1L, "chris");
        PostEntity postEntity = new PostEntity("caption", publisherEntity, post.getTimestamp());

        TagEntity createdTag = new TagEntity();
        createdTag.setName("newTag");

        PostEntity savedEntity = new PostEntity("caption", publisherEntity, post.getTimestamp());
        ReflectionTestUtils.setField(savedEntity, "id", 10L);
        Post expectedPost = new Post(10L, "caption", post.getTimestamp(), publisher);

        when(userJPARepository.findById(1L)).thenReturn(Optional.of(publisherEntity));
        when(postPersistenceMapper.toEntity(post, publisherEntity)).thenReturn(postEntity);
        when(tagJPARepository.findByName("newTag")).thenReturn(Optional.empty());
        when(tagJPARepository.save(any(TagEntity.class))).thenReturn(createdTag);
        when(postJPARepository.save(postEntity)).thenReturn(savedEntity);
        when(postPersistenceMapper.toDomain(savedEntity)).thenReturn(expectedPost);

        Post result = postRepository.save(post);

        assertEquals(expectedPost, result);
        verify(tagJPARepository).save(any(TagEntity.class));
        assertTrue(postEntity.getTags().contains(createdTag));
    }

    @Test
    void save_Should_Throw_RuntimeException_When_Publisher_Not_Found() {
        User publisher = new User(99L, "nobody", "nobody@example.com", null, "hash");
        Post post = new Post("caption", Instant.now(), publisher);

        when(userJPARepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> postRepository.save(post)
        );

        assertEquals("User not found", exception.getMessage());
        verify(postJPARepository, never()).save(any());
    }

    @Test
    void save_Should_Throw_RuntimeException_When_Product_Not_Found() {
        User publisher = new User(1L, "chris", "chris@example.com", null, "hash");
        Product product = new Product(99L, "Unknown", new Brand(1L, "Nike"));
        Post post = new Post("caption", Instant.now(), publisher);
        post.getProducts().add(product);

        UserEntity publisherEntity = makeUserEntity(1L, "chris");
        PostEntity postEntity = new PostEntity("caption", publisherEntity, post.getTimestamp());

        when(userJPARepository.findById(1L)).thenReturn(Optional.of(publisherEntity));
        when(postPersistenceMapper.toEntity(post, publisherEntity)).thenReturn(postEntity);
        when(productJPARepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> postRepository.save(post)
        );

        assertTrue(exception.getMessage().contains("Product not found"));
        verify(postJPARepository, never()).save(any());
    }
}
