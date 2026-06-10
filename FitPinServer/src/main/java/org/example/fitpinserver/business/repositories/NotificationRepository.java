package org.example.fitpinserver.business.repositories;

import org.example.fitpinserver.domain.models.Notification;

import java.util.List;

public interface NotificationRepository {
    List<Notification> findByRecipientId(Long recipientId);
    long countUnreadByRecipientId(Long recipientId);
    Notification save(Notification notification);
    void markAllAsRead(Long recipientId);
    void deleteByPostId(Long postId);
}
