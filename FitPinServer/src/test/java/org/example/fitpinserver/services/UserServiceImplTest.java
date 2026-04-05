package org.example.fitpinserver.services;

import org.example.fitpinserver.business.serviceImplementations.UserServiceImpl;
import org.example.fitpinserver.domain.UserRepository;
import org.example.fitpinserver.domain.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void registerUser_Should_Create_User_with_Available_Credentials() {
        User user = new User("chris", "chris@gmail.com", null, "123");
        User savedUser = new User(1L, "chris", "chris@gmail.com", null, "123");

        when(userRepository.existsByUsername("chris")).thenReturn(false);
        when(userRepository.existsByEmailAddress("chris@gmail.com")).thenReturn(false);
        when(userRepository.save(user)).thenReturn(savedUser);

        User result = userService.registerUser(user);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("chris", result.getUsername());
        assertEquals("chris@gmail.com", result.getEmailAddress());
        assertEquals("123", result.getPasswordHash());

        verify(userRepository).existsByUsername("chris");
        verify(userRepository).existsByEmailAddress("chris@gmail.com");
        verify(userRepository).save(user);
    }

    @Test
    void registerUser_Should_Throw_Runtime_Exception_with_Unavailable_Username() {
        User user = new User("chris", "chris@gmail.com", null, "123");

        when(userRepository.existsByUsername("chris")).thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.registerUser(user)
        );

        assertEquals("Username is already in use", exception.getMessage());

        verify(userRepository).existsByUsername("chris");
        verify(userRepository, never()).existsByEmailAddress("chris@gmail.com");
        verify(userRepository, never()).save(user);
    }

    @Test
    void registerUser_Should_Throw_Runtime_Exception_with_Unavailable_EmailAddress() {
        User user = new User("chris", "chris@gmail.com", null, "123");

        when(userRepository.existsByUsername("chris")).thenReturn(false);
        when(userRepository.existsByEmailAddress("chris@gmail.com")).thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.registerUser(user)
        );

        assertEquals("Email is already in use", exception.getMessage());

        verify(userRepository).existsByUsername("chris");
        verify(userRepository).existsByEmailAddress("chris@gmail.com");
        verify(userRepository, never()).save(user);
    }
}