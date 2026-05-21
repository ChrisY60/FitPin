package org.example.fitpinserver.mappers;

import org.example.fitpinserver.DAL.entities.UserEntity;
import org.example.fitpinserver.DAL.mappers.UserPersistenceMapper;
import org.example.fitpinserver.domain.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class UserPersistenceMapperTest {

    private final UserPersistenceMapper mapper = new UserPersistenceMapper();

    @Test
    void toDomain_mapsAllFields() {
        UserEntity entity = new UserEntity("alice", "alice@example.com", "bio text", "hashed_pw");
        ReflectionTestUtils.setField(entity, "id", 42L);

        User result = mapper.toDomain(entity);

        assertEquals(42L, result.getId());
        assertEquals("alice", result.getUsername());
        assertEquals("alice@example.com", result.getEmailAddress());
        assertEquals("bio text", result.getBio());
        assertEquals("hashed_pw", result.getPasswordHash());
    }

    @Test
    void toDomain_nullEntity_returnsNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    void toEntity_mapsAllFields() {
        User user = new User(1L, "alice", "alice@example.com", "bio text", "hashed_pw");

        UserEntity result = mapper.toEntity(user);

        // id is not mapped — it is assigned by the database on persist
        assertEquals("alice", result.getUsername());
        assertEquals("alice@example.com", result.getEmailAddress());
        assertEquals("bio text", result.getBio());
        assertEquals("hashed_pw", result.getPasswordHash());
    }

    @Test
    void toEntity_nullUser_returnsNull() {
        assertNull(mapper.toEntity(null));
    }
}
