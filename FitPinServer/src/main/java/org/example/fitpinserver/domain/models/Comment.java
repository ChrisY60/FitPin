package org.example.fitpinserver.domain.models;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

public class Comment {
    @Getter
    private Long id;

    @Getter
    @Setter
    private String content;

    @Getter
    @Setter
    private Instant timestamp;

    @Getter
    @Setter
    private User author;

    @Getter
    @Setter
    private Post post;

    public Comment() {
    }

    public Comment(Long id, String content, Instant timestamp, User author, Post post) {
        this.id = id;
        this.content = content;
        this.timestamp = timestamp;
        this.author = author;
        this.post = post;
    }

    public Comment(String content, Instant timestamp, User author, Post post) {
        this.content = content;
        this.timestamp = timestamp;
        this.author = author;
        this.post = post;
    }
}