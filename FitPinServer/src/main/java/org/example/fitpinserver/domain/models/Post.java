package org.example.fitpinserver.domain.models;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Post {
    @Getter
    private Long id;

    @Getter
    @Setter
    private String caption;

    @Getter
    @Setter
    private Instant timestamp;

    @Getter
    @Setter
    private User publisher;

    @Getter
    private List<Comment> comments = new ArrayList<>();

    @Getter
    private List<Product> products = new ArrayList<>();

    @Getter
    private List<PostLike> postLikes = new ArrayList<>();

    @Getter
    private List<Save> saves = new ArrayList<>();

    public Post() {
    }

    public Post(Long id, String caption, Instant timestamp, User publisher) {
        this.id = id;
        this.caption = caption;
        this.timestamp = timestamp;
        this.publisher = publisher;
    }

    public Post(String caption, Instant timestamp, User publisher) {
        this.caption = caption;
        this.timestamp = timestamp;
        this.publisher = publisher;
    }

    public void addSave(Save save) {
        this.saves.add(save);
        save.setPost(this);
    }

    public void removeSave(Save save) {
        if (this.saves.remove(save) && save.getPost() == this) {
            save.setPost(null);
        }
    }

    public void addPostLike(PostLike postLike) {
        this.postLikes.add(postLike);
        postLike.setPost(this);
    }

    public void removePostLike(PostLike postLike) {
        if (this.postLikes.remove(postLike) && postLike.getPost() == this) {
            postLike.setPost(null);
        }
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setPost(this);
    }

    public void removeComment(Comment comment) {
        if (this.comments.remove(comment) && comment.getPost() == this) {
            comment.setPost(null);
        }
    }

    public void addProduct(Product product) {
        this.products.add(product);
        product.getPosts().add(this);
    }

    public void removeProduct(Product product) {
        if (this.products.remove(product)) {
            product.getPosts().remove(this);
        }
    }
}
