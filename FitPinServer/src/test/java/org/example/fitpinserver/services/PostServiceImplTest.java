package org.example.fitpinserver.services;

import org.example.fitpinserver.business.repositories.PostRepository;
import org.example.fitpinserver.business.serviceImplementations.PostServiceImpl;
import org.example.fitpinserver.domain.models.Post;
import org.example.fitpinserver.domain.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

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
}
