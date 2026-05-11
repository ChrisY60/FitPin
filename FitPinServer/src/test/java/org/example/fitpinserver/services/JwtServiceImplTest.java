package org.example.fitpinserver.services;

import org.example.fitpinserver.business.serviceImplementations.JwtServiceImpl;
import org.example.fitpinserver.domain.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceImplTest {

    private JwtServiceImpl jwtService;
    private User user;

    @BeforeEach
    void setUp() {
        jwtService = new JwtServiceImpl("testsecretkey1234567890123456789012345", 86400000L);
        user = new User(1L, "chris", "chris@gmail.com", null, "hashed");
    }

    @Test
    void generateToken_ShouldReturnNonNullToken() {
        String token = jwtService.generateToken(user);
        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void extractUsername_ShouldReturnCorrectUsername() {
        String token = jwtService.generateToken(user);
        assertEquals("chris", jwtService.extractUsername(token));
    }

    @Test
    void isTokenValid_ShouldReturnTrueForFreshToken() {
        String token = jwtService.generateToken(user);
        assertTrue(jwtService.isTokenValid(token));
    }

    @Test
    void isTokenValid_ShouldReturnFalseForExpiredToken() {
        JwtServiceImpl shortLivedService = new JwtServiceImpl("testsecretkey1234567890123456789012345", -1000L);
        String expiredToken = shortLivedService.generateToken(user);
        assertFalse(jwtService.isTokenValid(expiredToken));
    }

    @Test
    void isTokenValid_ShouldReturnFalseForGarbageToken() {
        assertFalse(jwtService.isTokenValid("not.a.valid.token"));
    }
}
