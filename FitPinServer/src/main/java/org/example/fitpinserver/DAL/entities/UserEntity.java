package org.example.fitpinserver.DAL.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity {
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
    private List<PostEntity> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    private List<SaveEntity> saves = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    private List<PostLikeEntity> postLikes = new ArrayList<>();


    public UserEntity() {
    }

    public UserEntity(String username, String emailAddress, String bio, String passwordHash) {
        this.username = username;
        this.emailAddress = emailAddress;
        this.bio = bio;
        this.passwordHash = passwordHash;
    }

    public void addPost(PostEntity post){
        this.posts.add(post);
        post.setPublisher(this);
    }

    public void removePost(PostEntity post){
        if(this.posts.remove(post) && post.getPublisher() == this)
            post.setPublisher(null);
    }

    public void addSave(SaveEntity save){
        this.saves.add(save);
        save.setUser(this);
    }

    public void removeSave(SaveEntity save){
        if(this.saves.remove(save) && save.getUser() == this)
            save.setUser(null);
    }

    public void addPostLike(PostLikeEntity postLike){
        this.postLikes.add(postLike);
        postLike.setUser(this);
    }

    public void removePostLike(PostLikeEntity postLike){
        if(this.postLikes.remove(postLike) && postLike.getUser() == this)
            postLike.setUser(null);
    }

}
