package org.example.fitpinserver.DAL.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.fitpinserver.domain.enums.NotificationType;

import java.time.Instant;

@Entity
@Table(name = "notifications")
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Getter
    @Setter
    @Column(nullable = false)
    private Instant timestamp;

    @Getter
    @Setter
    @Column(name = "is_read", nullable = false)
    private boolean read;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    @Getter
    @Setter
    private UserEntity recipient;

    @ManyToOne
    @JoinColumn(name = "actor_id", nullable = false)
    @Getter
    @Setter
    private UserEntity actor;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @Getter
    @Setter
    private PostEntity post;

    public NotificationEntity() {
    }

    public NotificationEntity(NotificationType type, Instant timestamp, boolean read, UserEntity recipient, UserEntity actor, PostEntity post) {
        this.type = type;
        this.timestamp = timestamp;
        this.read = read;
        this.recipient = recipient;
        this.actor = actor;
        this.post = post;
    }
}
