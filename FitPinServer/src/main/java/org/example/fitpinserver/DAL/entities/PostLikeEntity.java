package org.example.fitpinserver.DAL.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "post_id"}), name = "post_likes")
@Entity
public class PostLikeEntity {
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
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    @Getter
    @Setter
    private PostEntity post;

    public PostLikeEntity() {
    }

    public PostLikeEntity(Instant timestamp, UserEntity user, PostEntity post) {
        this.timestamp = timestamp;
        this.user = user;
        this.post = post;
    }
}
