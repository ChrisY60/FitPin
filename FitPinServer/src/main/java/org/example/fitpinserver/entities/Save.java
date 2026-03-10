package org.example.fitpinserver.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.time.Instant;

@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "post_id"}))
@Entity
public class Save {
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
    @Getter
    @Setter
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public Save() {
    }

    public Save(Instant timestamp, User user, Post post) {
        this.timestamp = timestamp;
        this.user = user;
        this.post = post;
    }
}
