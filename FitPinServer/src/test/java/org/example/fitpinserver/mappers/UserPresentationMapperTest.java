package org.example.fitpinserver.mappers;

import org.example.fitpinserver.domain.models.User;
import org.example.fitpinserver.presentation.dtos.LoginUserResponseDTO;
import org.example.fitpinserver.presentation.dtos.RegisterUserRequestDTO;
import org.example.fitpinserver.presentation.dtos.RegisterUserResponseDTO;
import org.example.fitpinserver.presentation.mappers.UserPresentationMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserPresentationMapperTest {

    private final UserPresentationMapper mapper = new UserPresentationMapper();

    @Test
    void toRegisterUserResponseDTO_mapsAllFields() {
        User user = new User(1L, "alice", "alice@example.com", null, "hashed");

        RegisterUserResponseDTO result = mapper.toRegisterUserResponseDTO(user);

        assertEquals(1L, result.getId());
        assertEquals("alice", result.getUsername());
        assertEquals("alice@example.com", result.getEmailAddress());
    }

    @Test
    void toLoginUserResponseDTO_mapsAllFields() {
        User user = new User(2L, "bob", "bob@example.com", null, "hashed");

        LoginUserResponseDTO result = mapper.toLoginUserResponseDTO(user, "jwt.token.here");

        assertEquals("jwt.token.here", result.getToken());
        assertEquals(2L, result.getUserId());
        assertEquals("bob", result.getUsername());
    }

    @Test
    void toUserObjectFromRequestDTO_mapsAllFields() {
        RegisterUserRequestDTO dto = new RegisterUserRequestDTO("charlie", "charlie@example.com", "secret");

        User result = mapper.toUserObjectFromRequestDTO(dto);

        assertEquals("charlie", result.getUsername());
        assertEquals("charlie@example.com", result.getEmailAddress());
        assertNull(result.getBio());
        assertEquals("secret", result.getPasswordHash());
    }
}
