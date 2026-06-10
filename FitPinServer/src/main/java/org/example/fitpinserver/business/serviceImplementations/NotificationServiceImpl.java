package org.example.fitpinserver.business.serviceImplementations;

import org.example.fitpinserver.business.repositories.NotificationRepository;
import org.example.fitpinserver.business.services.NotificationService;
import org.example.fitpinserver.domain.enums.NotificationType;
import org.example.fitpinserver.domain.models.Comment;
import org.example.fitpinserver.domain.models.Notification;
import org.example.fitpinserver.domain.models.PostLike;
import org.example.fitpinserver.domain.models.User;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   SimpMessagingTemplate messagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    @Transactional
    public void notifyPostLiked(PostLike like) {
        User recipient = like.getPost().getPublisher();
        if (Objects.equals(like.getUser().getId(), recipient.getId())) {
            return;
        }
        notificationRepository.save(new Notification(
                NotificationType.LIKE,
                like.getTimestamp(),
                false,
                recipient,
                like.getUser(),
                like.getPost()
        ));
        pushUnreadCount(recipient);
    }

    @Override
    @Transactional
    public void notifyPostCommented(Comment comment) {
        User recipient = comment.getPost().getPublisher();
        if (Objects.equals(comment.getAuthor().getId(), recipient.getId())) {
            return;
        }
        notificationRepository.save(new Notification(
                NotificationType.COMMENT,
                comment.getTimestamp(),
                false,
                recipient,
                comment.getAuthor(),
                comment.getPost()
        ));
        pushUnreadCount(recipient);
    }

    private void pushUnreadCount(User recipient) {
        long count = notificationRepository.countUnreadByRecipientId(recipient.getId());
        messagingTemplate.convertAndSendToUser(
                recipient.getUsername(),
                "/queue/notifications",
                Map.of("count", count)
        );
    }

    @Override
    public List<Notification> getNotificationsForUser(Long userId) {
        return notificationRepository.findByRecipientId(userId);
    }

    @Override
    public long getUnreadCount(Long userId) {
        return notificationRepository.countUnreadByRecipientId(userId);
    }

    @Override
    @Transactional
    public void markAllAsRead(Long userId, String username) {
        notificationRepository.markAllAsRead(userId);
        messagingTemplate.convertAndSendToUser(username, "/queue/notifications", Map.of("count", 0));
    }
}
