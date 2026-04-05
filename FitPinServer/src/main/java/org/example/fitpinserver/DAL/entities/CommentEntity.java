package org.example.fitpinserver.DAL.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "comments")
public class CommentEntity {
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
    private UserEntity author;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    @Getter
    @Setter
    private PostEntity post;

    public CommentEntity() {
    }

    public CommentEntity(String content, UserEntity author, Instant timestamp, PostEntity post) {
        this.content = content;
        this.author = author;
        this.timestamp = timestamp;
        this.post = post;
    }


}
