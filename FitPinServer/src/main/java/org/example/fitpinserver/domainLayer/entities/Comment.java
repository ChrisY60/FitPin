package org.example.fitpinserver.domainLayer.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    @Setter
    @Column(nullable = false)
    private String content;

    @Getter
    @Setter
    @Column(nullable = false)
    private Instant timestamp;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    @Getter
    @Setter
    private User author;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    @Getter
    @Setter
    private Post post;

    public Comment() {
    }

    public Comment(String content, User author, Instant timestamp, Post post) {
        this.content = content;
        this.author = author;
        this.timestamp = timestamp;
        this.post = post;
    }


}
