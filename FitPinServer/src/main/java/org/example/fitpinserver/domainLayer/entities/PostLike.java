package org.example.fitpinserver.domainLayer.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "post_id"}))
@Entity
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    @Setter
    @Column(nullable = false)
    private Instant timestamp;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Getter
    @Setter
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    @Getter
    @Setter
    private Post post;

    public PostLike() {
    }

    public PostLike(Instant timestamp, User user, Post post) {
        this.timestamp = timestamp;
        this.user = user;
        this.post = post;
    }
}
