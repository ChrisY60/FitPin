package org.example.fitpinserver.services;

import org.example.fitpinserver.business.repositories.PostRepository;
import org.example.fitpinserver.business.repositories.ProductRepository;
import org.example.fitpinserver.business.repositories.UserRepository;
import org.example.fitpinserver.business.serviceImplementations.PostServiceImpl;
import org.example.fitpinserver.domain.models.Brand;
import org.example.fitpinserver.domain.models.Post;
import org.example.fitpinserver.domain.models.Product;
import org.example.fitpinserver.domain.models.Tag;
import org.example.fitpinserver.domain.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private PostServiceImpl postService;

    @Test
    void getAllPosts_ShouldReturnAllPostsFromRepository() {
        User publisher = new User(1L, "chris", "chris@gmail.com", null, "hashed");
        List<Post> expected = List.of(
                new Post(1L, "caption1", Instant.now(), publisher),
                new Post(2L, "caption2", Instant.now(), publisher)
        );
        when(postRepository.findAll()).thenReturn(expected);

        List<Post> result = postService.getAllPosts();

        assertEquals(2, result.size());
        assertEquals(expected, result);
        verify(postRepository).findAll();
    }

    @Test
    void getAllPosts_ShouldReturnEmptyListWhenNoPostsExist() {
        when(postRepository.findAll()).thenReturn(List.of());

        List<Post> result = postService.getAllPosts();

        assertEquals(0, result.size());
        verify(postRepository).findAll();
    }

    @Test
    void getPostsByUsername_Should_Return_Posts_For_Existing_User() {
        User publisher = new User(1L, "chris", "chris@gmail.com", null, "hashed");
        List<Post> expected = List.of(
                new Post(1L, "caption1", Instant.now(), publisher),
                new Post(2L, "caption2", Instant.now(), publisher)
        );
        when(postRepository.findByPublisherUsername("chris")).thenReturn(expected);

        List<Post> result = postService.getPostsByUsername("chris");

        assertEquals(2, result.size());
        assertEquals(expected, result);
        verify(postRepository).findByPublisherUsername("chris");
    }

    @Test
    void getPostsByUsername_Should_Return_Empty_List_When_User_Has_No_Posts() {
        when(postRepository.findByPublisherUsername("chris")).thenReturn(List.of());

        List<Post> result = postService.getPostsByUsername("chris");

        assertTrue(result.isEmpty());
        verify(postRepository).findByPublisherUsername("chris");
    }

    @Test
    void createPost_Should_Save_And_Return_Post_With_Products_And_Tags() {
        User publisher = new User(1L, "chris", "chris@gmail.com", null, "hashed");
        Product product = new Product(2L, "Shirt", new Brand(1L, "Nike"));
        Post savedPost = new Post(10L, "my caption", Instant.now(), publisher);

        when(userRepository.findByUsername("chris")).thenReturn(Optional.of(publisher));
        when(productRepository.findAllByIds(List.of(2L))).thenReturn(List.of(product));
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        Post result = postService.createPost("chris", "my caption", "https://img.jpg", List.of(2L), List.of("fashion"));

        assertNotNull(result);
        assertEquals(10L, result.getId());

        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).save(captor.capture());
        Post captured = captor.getValue();
        assertEquals("my caption", captured.getCaption());
        assertEquals("https://img.jpg", captured.getImageUrl());
        assertEquals(1, captured.getProducts().size());
        assertEquals(2L, captured.getProducts().get(0).getId());
        assertEquals("Shirt", captured.getProducts().get(0).getName());
        assertEquals(1, captured.getTags().size());
        assertEquals("fashion", captured.getTags().get(0).getName());
    }

    @Test
    void createPost_Should_Throw_RuntimeException_When_Publisher_Not_Found() {
        when(userRepository.findByUsername("nobody")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> postService.createPost("nobody", "caption", "img.jpg", List.of(), List.of())
        );

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findByUsername("nobody");
        verify(postRepository, never()).save(any());
    }

    @Test
    void createPost_Should_Ignore_Blank_And_Null_TagNames() {
        User publisher = new User(1L, "chris", "chris@gmail.com", null, "hashed");
        Post savedPost = new Post(10L, "caption", Instant.now(), publisher);

        when(userRepository.findByUsername("chris")).thenReturn(Optional.of(publisher));
        when(productRepository.findAllByIds(List.of())).thenReturn(List.of());
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        postService.createPost("chris", "caption", "img.jpg", List.of(), Arrays.asList("  ", null, "fashion"));

        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).save(captor.capture());
        List<Tag> tags = captor.getValue().getTags();
        assertEquals(1, tags.size());
        assertEquals("fashion", tags.get(0).getName());
    }

    @Test
    void createPost_Should_Work_With_Empty_ProductIds_And_TagNames() {
        User publisher = new User(1L, "chris", "chris@gmail.com", null, "hashed");
        Post savedPost = new Post(10L, "caption", Instant.now(), publisher);

        when(userRepository.findByUsername("chris")).thenReturn(Optional.of(publisher));
        when(productRepository.findAllByIds(List.of())).thenReturn(List.of());
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        Post result = postService.createPost("chris", "caption", "img.jpg", List.of(), List.of());

        assertNotNull(result);
        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).save(captor.capture());
        assertTrue(captor.getValue().getProducts().isEmpty());
        assertTrue(captor.getValue().getTags().isEmpty());
    }
}
