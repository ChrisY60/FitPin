package org.example.fitpinserver.DAL.entities;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tags")
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "tags")
    @Getter
    private List<PostEntity> posts = new ArrayList<>();

    public void addPost(PostEntity post) {
        this.posts.add(post);
        post.getTags().add(this);
    }

    public void removePost(PostEntity post) {
        if (this.posts.remove(post)) {
            post.getTags().remove(this);
        }
    }
}