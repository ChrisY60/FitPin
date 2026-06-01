package org.example.fitpinserver.DAL.mappers;

import org.example.fitpinserver.DAL.entities.UserEntity;
import org.example.fitpinserver.domain.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceMapper {

    public User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        User user = new User(
                entity.getId(),
                entity.getUsername(),
                entity.getEmailAddress(),
                entity.getBio(),
                entity.getPasswordHash()
        );
        user.setProfilePictureUrl(entity.getProfilePictureUrl());
        return user;
    }

    public UserEntity toEntity(User domain) {
        if (domain == null) {
            return null;
        }

        UserEntity entity = new UserEntity(
                domain.getUsername(),
                domain.getEmailAddress(),
                domain.getBio(),
                domain.getPasswordHash()
        );

        return entity;
    }
}