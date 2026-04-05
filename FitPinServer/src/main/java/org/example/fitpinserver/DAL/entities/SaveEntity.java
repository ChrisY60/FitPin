package org.example.fitpinserver.DAL.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "post_id"}), name = "saves")
@Entity
public class SaveEntity {
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
    @Getter
    @Setter
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;

    public SaveEntity() {
    }

    public SaveEntity(Instant timestamp, UserEntity user, PostEntity post) {
        this.timestamp = timestamp;
        this.user = user;
        this.post = post;
    }
}
