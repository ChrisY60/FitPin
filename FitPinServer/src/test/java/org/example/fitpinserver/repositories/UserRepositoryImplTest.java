package org.example.fitpinserver.repositories;

import org.example.fitpinserver.DAL.entities.UserEntity;
import org.example.fitpinserver.DAL.mappers.UserPersistenceMapper;
import org.example.fitpinserver.DAL.repositories.UserJPARepository;
import org.example.fitpinserver.DAL.repositories.UserRepositoryImpl;
import org.example.fitpinserver.domain.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest {

    @Mock
    private UserJPARepository userJPARepository;

    @Mock
    private UserPersistenceMapper userPersistenceMapper;

    @InjectMocks
    private UserRepositoryImpl userRepository;

    private UserEntity makeEntity(Long id, String username) {
        UserEntity entity = new UserEntity(username, username + "@example.com", null, "hash");
        ReflectionTestUtils.setField(entity, "id", id);
        return entity;
    }

    private User makeDomain(Long id, String username) {
        return new User(id, username, username + "@example.com", null, "hash");
    }

    @Test
    void save_Should_Return_Mapped_User() {
        User user = makeDomain(null, "chris");
        UserEntity entity = makeEntity(1L, "chris");
        User savedUser = makeDomain(1L, "chris");

        when(userPersistenceMapper.toEntity(user)).thenReturn(entity);
        when(userJPARepository.save(entity)).thenReturn(entity);
        when(userPersistenceMapper.toDomain(entity)).thenReturn(savedUser);

        User result = userRepository.save(user);

        assertEquals(savedUser, result);
        verify(userJPARepository).save(entity);
    }

    @Test
    void findById_Should_Return_User_When_Found() {
        UserEntity entity = makeEntity(1L, "chris");
        User user = makeDomain(1L, "chris");

        when(userJPARepository.findById(1L)).thenReturn(Optional.of(entity));
        when(userPersistenceMapper.toDomain(entity)).thenReturn(user);

        Optional<User> result = userRepository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void findById_Should_Return_Empty_When_Not_Found() {
        when(userJPARepository.findById(99L)).thenReturn(Optional.empty());

        Optional<User> result = userRepository.findById(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    void findByUsername_Should_Return_User_When_Found() {
        UserEntity entity = makeEntity(1L, "chris");
        User user = makeDomain(1L, "chris");

        when(userJPARepository.findByUsername("chris")).thenReturn(Optional.of(entity));
        when(userPersistenceMapper.toDomain(entity)).thenReturn(user);

        Optional<User> result = userRepository.findByUsername("chris");

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void findByUsername_Should_Return_Empty_When_Not_Found() {
        when(userJPARepository.findByUsername("nobody")).thenReturn(Optional.empty());

        Optional<User> result = userRepository.findByUsername("nobody");

        assertTrue(result.isEmpty());
    }

    @Test
    void findByEmailAddress_Should_Return_User_When_Found() {
        UserEntity entity = makeEntity(1L, "chris");
        User user = makeDomain(1L, "chris");

        when(userJPARepository.findByEmailAddress("chris@example.com")).thenReturn(Optional.of(entity));
        when(userPersistenceMapper.toDomain(entity)).thenReturn(user);

        Optional<User> result = userRepository.findByEmailAddress("chris@example.com");

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void findByEmailAddress_Should_Return_Empty_When_Not_Found() {
        when(userJPARepository.findByEmailAddress("nobody@example.com")).thenReturn(Optional.empty());

        Optional<User> result = userRepository.findByEmailAddress("nobody@example.com");

        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_Should_Return_All_Mapped_Users() {
        UserEntity e1 = makeEntity(1L, "alice");
        UserEntity e2 = makeEntity(2L, "bob");
        User u1 = makeDomain(1L, "alice");
        User u2 = makeDomain(2L, "bob");

        when(userJPARepository.findAll()).thenReturn(List.of(e1, e2));
        when(userPersistenceMapper.toDomain(e1)).thenReturn(u1);
        when(userPersistenceMapper.toDomain(e2)).thenReturn(u2);

        List<User> result = userRepository.findAll();

        assertEquals(2, result.size());
        assertEquals(u1, result.get(0));
        assertEquals(u2, result.get(1));
    }

    @Test
    void deleteById_Should_Delegate_To_JPA() {
        userRepository.deleteById(1L);

        verify(userJPARepository).deleteById(1L);
    }

    @Test
    void existsByUsername_Should_Return_True_When_Username_Exists() {
        when(userJPARepository.existsByUsername("chris")).thenReturn(true);

        assertTrue(userRepository.existsByUsername("chris"));
    }

    @Test
    void existsByUsername_Should_Return_False_When_Username_Does_Not_Exist() {
        when(userJPARepository.existsByUsername("nobody")).thenReturn(false);

        assertFalse(userRepository.existsByUsername("nobody"));
    }

    @Test
    void existsByEmailAddress_Should_Return_True_When_Email_Exists() {
        when(userJPARepository.existsByEmailAddress("chris@example.com")).thenReturn(true);

        assertTrue(userRepository.existsByEmailAddress("chris@example.com"));
    }

    @Test
    void existsByEmailAddress_Should_Return_False_When_Email_Does_Not_Exist() {
        when(userJPARepository.existsByEmailAddress("nobody@example.com")).thenReturn(false);

        assertFalse(userRepository.existsByEmailAddress("nobody@example.com"));
    }

    @Test
    void updateProfile_Should_Update_And_Return_User() {
        UserEntity entity = makeEntity(1L, "chris");
        UserEntity savedEntity = makeEntity(1L, "chris");
        User updatedUser = new User(1L, "chris", "chris@example.com", "new bio", "hash");
        updatedUser.setProfilePictureUrl("https://example.com/pic.jpg");

        when(userJPARepository.findById(1L)).thenReturn(Optional.of(entity));
        when(userJPARepository.save(entity)).thenReturn(savedEntity);
        when(userPersistenceMapper.toDomain(savedEntity)).thenReturn(updatedUser);

        User result = userRepository.updateProfile(1L, "new bio", "https://example.com/pic.jpg");

        assertEquals("new bio", result.getBio());
        assertEquals("https://example.com/pic.jpg", result.getProfilePictureUrl());
        verify(userJPARepository).save(entity);
    }

    @Test
    void updateProfile_Should_Throw_RuntimeException_When_User_Not_Found() {
        when(userJPARepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userRepository.updateProfile(99L, "bio", "pic.jpg")
        );

        assertEquals("User not found", exception.getMessage());
        verify(userJPARepository, never()).save(any());
    }
}
