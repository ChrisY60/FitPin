package org.example.fitpinserver.domainLayer.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Setter
    @Getter
    @Column(nullable = false, unique = true)
    private String username;

    @Setter
    @Getter
    @Column(nullable = false, unique = true)
    private String emailAddress;

    @Setter
    @Getter
    @Column()
    private String bio;

    @Setter
    @Getter
    @Column(nullable = false)
    private String passwordHash;

    @OneToMany(mappedBy = "publisher", cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    private List<Save> saves = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    private List<PostLike> postLikes = new ArrayList<>();


    public User() {
    }

    public User(String username, String emailAddress, String bio, String passwordHash) {
        this.username = username;
        this.emailAddress = emailAddress;
        this.bio = bio;
        this.passwordHash = passwordHash;
    }

    public void addPost(Post post){
        this.posts.add(post);
        post.setPublisher(this);
    }

    public void removePost(Post post){
        if(this.posts.remove(post) && post.getPublisher() == this)
            post.setPublisher(null);
    }

    public void addSave(Save save){
        this.saves.add(save);
        save.setUser(this);
    }

    public void removeSave(Save save){
        if(this.saves.remove(save) && save.getUser() == this)
            save.setUser(null);
    }

    public void addPostLike(PostLike postLike){
        this.postLikes.add(postLike);
        postLike.setUser(this);
    }

    public void removePostLike(PostLike postLike){
        if(this.postLikes.remove(postLike) && postLike.getUser() == this)
            postLike.setUser(null);
    }

}
