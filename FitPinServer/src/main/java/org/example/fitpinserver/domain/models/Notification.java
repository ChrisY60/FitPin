package org.example.fitpinserver.domain.models;

import lombok.Getter;
import lombok.Setter;
import org.example.fitpinserver.domain.enums.NotificationType;

import java.time.Instant;

public class Notification {
    @Getter
    private Long id;

    @Getter
    @Setter
    private NotificationType type;

    @Getter
    @Setter
    private Instant timestamp;

    @Getter
    @Setter
    private boolean read;

    @Getter
    @Setter
    private User recipient;

    @Getter
    @Setter
    private User actor;

    @Getter
    @Setter
    private Post post;

    public Notification() {
    }

    public Notification(Long id, NotificationType type, Instant timestamp, boolean read, User recipient, User actor, Post post) {
        this.id = id;
        this.type = type;
        this.timestamp = timestamp;
        this.read = read;
        this.recipient = recipient;
        this.actor = actor;
        this.post = post;
    }

    public Notification(NotificationType type, Instant timestamp, boolean read, User recipient, User actor, Post post) {
        this.type = type;
        this.timestamp = timestamp;
        this.read = read;
        this.recipient = recipient;
        this.actor = actor;
        this.post = post;
    }
}
