package org.example.fitpinserver.business.services;

import org.example.fitpinserver.domain.models.*;

import java.util.List;

public interface NotificationService {
    void notifyPostLiked(PostLike like);
    void notifyPostCommented(Comment comment);
    List<Notification> getNotificationsForUser(Long userId);
    long getUnreadCount(Long userId);
    void markAllAsRead(Long userId, String username);
}
