package org.example.fitpinserver.services;

import org.example.fitpinserver.business.serviceImplementations.UserServiceImpl;
import org.example.fitpinserver.domain.repositories.UserRepository;
import org.example.fitpinserver.domain.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void registerUser_Should_Create_User_with_Available_Credentials() {
        User user = new User("chris", "chris@gmail.com", null, "123");
        User savedUser = new User(1L, "chris", "chris@gmail.com", null, "hashed123");

        when(userRepository.existsByUsername("chris")).thenReturn(false);
        when(userRepository.existsByEmailAddress("chris@gmail.com")).thenReturn(false);
        when(passwordEncoder.encode("123")).thenReturn("hashed123");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.registerUser(user);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("chris", result.getUsername());
        assertEquals("chris@gmail.com", result.getEmailAddress());
        assertEquals("hashed123", result.getPasswordHash());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).existsByUsername("chris");
        verify(userRepository).existsByEmailAddress("chris@gmail.com");
        verify(passwordEncoder).encode("123");
        verify(userRepository).save(userCaptor.capture());

        User savedArgument = userCaptor.getValue();
        assertEquals("chris", savedArgument.getUsername());
        assertEquals("chris@gmail.com", savedArgument.getEmailAddress());
        assertEquals("hashed123", savedArgument.getPasswordHash());
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
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any(User.class));
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
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void loginUser_Should_Login_With_Username_When_Credentials_Are_Correct() {
        User existingUser = new User(1L, "chris", "chris@gmail.com", null, "hashed123");

        when(userRepository.findByUsername("chris")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("123", "hashed123")).thenReturn(true);

        User result = userService.loginUser("chris", "123");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("chris", result.getUsername());
        assertEquals("chris@gmail.com", result.getEmailAddress());

        verify(userRepository).findByUsername("chris");
        verify(passwordEncoder).matches("123", "hashed123");
    }

    @Test
    void loginUser_Should_Login_With_Email_When_Credentials_Are_Correct() {
        User existingUser = new User(1L, "chris", "chris@gmail.com", null, "hashed123");

        when(userRepository.findByUsername("chris@gmail.com")).thenReturn(Optional.empty());
        when(userRepository.findByEmailAddress("chris@gmail.com")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("123", "hashed123")).thenReturn(true);

        User result = userService.loginUser("chris@gmail.com", "123");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("chris", result.getUsername());
        assertEquals("chris@gmail.com", result.getEmailAddress());

        verify(userRepository).findByUsername("chris@gmail.com");
        verify(userRepository).findByEmailAddress("chris@gmail.com");
        verify(passwordEncoder).matches("123", "hashed123");
    }

    @Test
    void loginUser_Should_Throw_Runtime_Exception_When_User_Does_Not_Exist() {
        when(userRepository.findByUsername("chris")).thenReturn(Optional.empty());
        when(userRepository.findByEmailAddress("chris")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.loginUser("chris", "123")
        );

        assertEquals("Invalid username/email or password", exception.getMessage());

        verify(userRepository).findByUsername("chris");
        verify(userRepository).findByEmailAddress("chris");
        verify(passwordEncoder, never()).matches(any(), any());
    }

    @Test
    void loginUser_Should_Throw_Runtime_Exception_When_Password_Is_Incorrect() {
        User existingUser = new User(1L, "chris", "chris@gmail.com", null, "hashed123");

        when(userRepository.findByUsername("chris")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("wrongPassword", "hashed123")).thenReturn(false);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.loginUser("chris", "wrongPassword")
        );

        assertEquals("Invalid username/email or password", exception.getMessage());

        verify(userRepository).findByUsername("chris");
        verify(passwordEncoder).matches("wrongPassword", "hashed123");
    }
}