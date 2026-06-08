package org.example.fitpinserver.DAL.mappers;

import org.example.fitpinserver.DAL.entities.CommentEntity;
import org.example.fitpinserver.DAL.entities.PostEntity;
import org.example.fitpinserver.DAL.entities.UserEntity;
import org.example.fitpinserver.domain.models.Comment;
import org.example.fitpinserver.domain.models.User;
import org.springframework.stereotype.Component;

@Component
public class CommentPersistenceMapper {

    public Comment toDomain(CommentEntity entity) {
        if (entity == null) {
            return null;
        }

        UserEntity author = entity.getAuthor();
        User authorDomain = new User(
                author.getId(),
                author.getUsername(),
                author.getEmailAddress(),
                author.getBio(),
                author.getPasswordHash()
        );
        authorDomain.setProfilePictureUrl(author.getProfilePictureUrl());

        return new Comment(entity.getId(), entity.getContent(), entity.getTimestamp(), authorDomain, null);
    }

    public CommentEntity toEntity(Comment comment, UserEntity author, PostEntity post) {
        return new CommentEntity(comment.getContent(), author, comment.getTimestamp(), post);
    }
}
