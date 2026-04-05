package org.example.fitpinserver.domain.models;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

public class PostLike {
    @Getter
    private Long id;

    @Getter
    @Setter
    private Instant timestamp;

    @Getter
    @Setter
    private User user;

    @Getter
    @Setter
    private Post post;

    public PostLike() {
    }

    public PostLike(Long id, Instant timestamp, User user, Post post) {
        this.id = id;
        this.timestamp = timestamp;
        this.user = user;
        this.post = post;
    }

    public PostLike(Instant timestamp, User user, Post post) {
        this.timestamp = timestamp;
        this.user = user;
        this.post = post;
    }
}