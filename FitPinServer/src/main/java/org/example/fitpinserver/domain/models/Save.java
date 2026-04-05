package org.example.fitpinserver.domain.models;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

public class Save {
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

    public Save() {
    }

    public Save(Long id, Instant timestamp, User user, Post post) {
        this.id = id;
        this.timestamp = timestamp;
        this.user = user;
        this.post = post;
    }

    public Save(Instant timestamp, User user, Post post) {
        this.timestamp = timestamp;
        this.user = user;
        this.post = post;
    }
}