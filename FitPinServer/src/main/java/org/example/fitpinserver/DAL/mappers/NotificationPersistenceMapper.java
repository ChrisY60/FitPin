package org.example.fitpinserver.DAL.mappers;

import org.example.fitpinserver.DAL.entities.NotificationEntity;
import org.example.fitpinserver.DAL.entities.PostEntity;
import org.example.fitpinserver.DAL.entities.UserEntity;
import org.example.fitpinserver.domain.models.Notification;
import org.example.fitpinserver.domain.models.Post;
import org.example.fitpinserver.domain.models.User;
import org.springframework.stereotype.Component;

@Component
public class NotificationPersistenceMapper {

    public Notification toDomain(NotificationEntity entity) {
        if (entity == null) {
            return null;
        }

        User recipientDomain = toUserDomain(entity.getRecipient());
        User actorDomain = toUserDomain(entity.getActor());

        PostEntity postEntity = entity.getPost();
        Post postDomain = null;
        if (postEntity != null) {
            postDomain = new Post(postEntity.getId(), postEntity.getCaption(), postEntity.getTimestamp(), null);
            postDomain.setImageUrl(postEntity.getImageUrl());
        }

        return new Notification(entity.getId(), entity.getType(), entity.getTimestamp(), entity.isRead(),
                recipientDomain, actorDomain, postDomain);
    }

    public NotificationEntity toEntity(Notification notification, UserEntity recipient, UserEntity actor, PostEntity post) {
        return new NotificationEntity(notification.getType(), notification.getTimestamp(), notification.isRead(),
                recipient, actor, post);
    }

    private User toUserDomain(UserEntity entity) {
        User user = new User(entity.getId(), entity.getUsername(), entity.getEmailAddress(), entity.getBio(), entity.getPasswordHash());
        user.setProfilePictureUrl(entity.getProfilePictureUrl());
        return user;
    }
}
