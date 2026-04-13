package org.example.fitpinserver.domain.models;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Tag {
    @Getter
    private Long id;

    @Getter
    private String name;

    @Getter
    private List<Post> posts = new ArrayList<>();

    public void addPost(Post post) {
        this.posts.add(post);
        post.getTags().add(this);
    }

    public void removePost(Post post) {
        if (this.posts.remove(post)) {
            post.getTags().remove(this);
        }
    }
}
