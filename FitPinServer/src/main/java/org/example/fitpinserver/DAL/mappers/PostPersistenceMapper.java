package org.example.fitpinserver.DAL.mappers;

import org.example.fitpinserver.DAL.entities.PostEntity;
import org.example.fitpinserver.DAL.entities.ProductEntity;
import org.example.fitpinserver.DAL.entities.TagEntity;
import org.example.fitpinserver.DAL.entities.UserEntity;
import org.example.fitpinserver.domain.models.Brand;
import org.example.fitpinserver.domain.models.Comment;
import org.example.fitpinserver.domain.models.Post;
import org.example.fitpinserver.domain.models.PostLike;
import org.example.fitpinserver.domain.models.Product;
import org.example.fitpinserver.domain.models.Tag;
import org.example.fitpinserver.domain.models.User;
import org.springframework.stereotype.Component;

@Component
public class PostPersistenceMapper {

    public Post toDomain(PostEntity entity) {
        if (entity == null) {
            return null;
        }

        UserEntity publisher = entity.getPublisher();
        User publisherDomain = new User(
                publisher.getId(),
                publisher.getUsername(),
                publisher.getEmailAddress(),
                publisher.getBio(),
                publisher.getPasswordHash()
        );

        Post post = new Post(entity.getId(), entity.getCaption(), entity.getTimestamp(), publisherDomain);
        post.setImageUrl(entity.getImageUrl());

        for (TagEntity tagEntity : entity.getTags()) {
            post.getTags().add(new Tag(tagEntity.getId(), tagEntity.getName()));
        }

        for (ProductEntity productEntity : entity.getProducts()) {
            Brand brand = new Brand(productEntity.getBrand().getId(), productEntity.getBrand().getName());
            post.getProducts().add(new Product(productEntity.getId(), productEntity.getName(), brand));
        }

        entity.getPostLikes().forEach(like -> post.getPostLikes().add(new PostLike()));
        entity.getComments().forEach(comment -> post.getComments().add(new Comment()));

        return post;
    }
}
