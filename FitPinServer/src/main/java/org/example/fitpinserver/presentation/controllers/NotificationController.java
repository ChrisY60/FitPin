package org.example.fitpinserver.presentation.controllers;

import org.example.fitpinserver.business.services.NotificationService;
import org.example.fitpinserver.business.services.UserService;
import org.example.fitpinserver.domain.models.Notification;
import org.example.fitpinserver.domain.models.Post;
import org.example.fitpinserver.presentation.dtos.NotificationResponseDTO;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @GetMapping
    public List<NotificationResponseDTO> getNotifications() {
        Long userId = getCurrentUserId();
        return notificationService.getNotificationsForUser(userId).stream()
                .map(this::toNotificationResponseDTO)
                .toList();
    }

    @GetMapping("/unread-count")
    public Map<String, Long> getUnreadCount() {
        Long userId = getCurrentUserId();
        return Map.of("count", notificationService.getUnreadCount(userId));
    }

    @PutMapping("/{notificationId}/read")
    public void markAsRead(@PathVariable Long notificationId) {
        Long userId = getCurrentUserId();
        notificationService.markAsRead(notificationId, userId);
    }

    @PutMapping("/read-all")
    public void markAllAsRead() {
        Long userId = getCurrentUserId();
        notificationService.markAllAsRead(userId);
    }

    private NotificationResponseDTO toNotificationResponseDTO(Notification notification) {
        Post post = notification.getPost();
        return new NotificationResponseDTO(
                notification.getId(),
                notification.getType().name(),
                notification.getTimestamp(),
                notification.isRead(),
                notification.getActor().getUsername(),
                notification.getActor().getProfilePictureUrl(),
                post != null ? post.getId() : null,
                post != null ? post.getImageUrl() : null
        );
    }

    private Long getCurrentUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getUserByUsername(username).getId();
    }
}
