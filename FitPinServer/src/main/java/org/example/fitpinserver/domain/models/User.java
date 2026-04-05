package org.example.fitpinserver.domain.models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class User {
    @Getter
    private Long id;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String emailAddress;

    @Getter
    @Setter
    private String bio;

    @Getter
    @Setter
    private String passwordHash;

    @Getter
    private List<Post> posts = new ArrayList<>();

    @Getter
    private List<Save> saves = new ArrayList<>();

    @Getter
    private List<PostLike> postLikes = new ArrayList<>();

    public User() {
    }

    public User(Long id, String username, String emailAddress, String bio, String passwordHash) {
        this.id = id;
        this.username = username;
        this.emailAddress = emailAddress;
        this.bio = bio;
        this.passwordHash = passwordHash;
    }

    public User(String username, String emailAddress, String bio, String passwordHash) {
        this.username = username;
        this.emailAddress = emailAddress;
        this.bio = bio;
        this.passwordHash = passwordHash;
    }

    public void addPost(Post post) {
        this.posts.add(post);
        post.setPublisher(this);
    }

    public void removePost(Post post) {
        if (this.posts.remove(post) && post.getPublisher() == this) {
            post.setPublisher(null);
        }
    }

    public void addSave(Save save) {
        this.saves.add(save);
        save.setUser(this);
    }

    public void removeSave(Save save) {
        if (this.saves.remove(save) && save.getUser() == this) {
            save.setUser(null);
        }
    }

    public void addPostLike(PostLike postLike) {
        this.postLikes.add(postLike);
        postLike.setUser(this);
    }

    public void removePostLike(PostLike postLike) {
        if (this.postLikes.remove(postLike) && postLike.getUser() == this) {
            postLike.setUser(null);
        }
    }
}