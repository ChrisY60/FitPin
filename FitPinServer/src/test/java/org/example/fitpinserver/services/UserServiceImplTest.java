package org.example.fitpinserver.services;

import org.example.fitpinserver.businessLayer.serviceImplementations.UserServiceImpl;
import org.example.fitpinserver.domainLayer.entities.User;
import org.example.fitpinserver.dataAccessLayer.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void registerUser_Should_Create_User_with_Available_Credentials(){
        User user = new User("chris", "chris@gmail.com", null, "123");

        when(userRepository.existsByUsername("chris")).thenReturn(false);
        when(userRepository.existsByEmailAddress("chris@gmail.com")).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.registerUser(user);
        assertNotNull(result);
        assertEquals("chris", result.getUsername());
        assertEquals("chris@gmail.com", result.getEmailAddress());
        //when I add hashing I will make sure to fix this test as well
        assertEquals("123", result.getPasswordHash());

        verify(userRepository).existsByUsername("chris");
        verify(userRepository).existsByEmailAddress("chris@gmail.com");
        verify(userRepository).save(user);
    }

    @Test
    void registerUser_Should_Throw_Runtime_Exception_with_Unavailable_Username() {
        User user = new User("chris", "chris@gmail.com", null, "123");

        when(userRepository.existsByUsername("chris")).thenReturn(true);

        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> userService.registerUser(user)
        );

        assertEquals("Username is already in use", exception.getMessage());


        verify(userRepository).existsByUsername("chris");
        //I check for the username first and below I just make sure the other checks in the service do not occur and the repository doesnt save the user
        verify(userRepository, org.mockito.Mockito.never()).existsByEmailAddress("chris@gmail.com");
        verify(userRepository, org.mockito.Mockito.never()).save(user);
    }

    @Test
    void registerUser_Should_Throw_Runtime_Exception_with_Unavailable_EmailAddress() {
        User user = new User("chris", "chris@gmail.com", null, "123");

        when(userRepository.existsByUsername("chris")).thenReturn(false);
        when(userRepository.existsByEmailAddress("chris@gmail.com")).thenReturn(true);

        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> userService.registerUser(user)
        );

        assertEquals("Email is already in use", exception.getMessage());


        verify(userRepository).existsByUsername("chris");
        verify(userRepository).existsByEmailAddress("chris@gmail.com");
        //again, making sure that the repository doesnt try to create the user
        verify(userRepository, org.mockito.Mockito.never()).save(user);
    }
}
