package org.example.fitpinserver.DAL.repositories;

import org.example.fitpinserver.DAL.entities.NotificationEntity;
import org.example.fitpinserver.DAL.entities.PostEntity;
import org.example.fitpinserver.DAL.entities.UserEntity;
import org.example.fitpinserver.DAL.mappers.NotificationPersistenceMapper;
import org.example.fitpinserver.business.repositories.NotificationRepository;
import org.example.fitpinserver.domain.models.Notification;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NotificationRepositoryImpl implements NotificationRepository {

    private final NotificationJPARepository notificationJPARepository;
    private final NotificationPersistenceMapper notificationPersistenceMapper;
    private final UserJPARepository userJPARepository;
    private final PostJPARepository postJPARepository;

    public NotificationRepositoryImpl(NotificationJPARepository notificationJPARepository,
                                       NotificationPersistenceMapper notificationPersistenceMapper,
                                       UserJPARepository userJPARepository,
                                       PostJPARepository postJPARepository) {
        this.notificationJPARepository = notificationJPARepository;
        this.notificationPersistenceMapper = notificationPersistenceMapper;
        this.userJPARepository = userJPARepository;
        this.postJPARepository = postJPARepository;
    }

    @Override
    public List<Notification> findByRecipientId(Long recipientId) {
        return notificationJPARepository.findByRecipient_IdOrderByTimestampDesc(recipientId).stream()
                .map(notificationPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public long countUnreadByRecipientId(Long recipientId) {
        return notificationJPARepository.countByRecipient_IdAndReadFalse(recipientId);
    }

    @Override
    public Notification save(Notification notification) {
        UserEntity recipientEntity = userJPARepository.findById(notification.getRecipient().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserEntity actorEntity = userJPARepository.findById(notification.getActor().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        PostEntity postEntity = null;
        if (notification.getPost() != null) {
            postEntity = postJPARepository.findById(notification.getPost().getId())
                    .orElseThrow(() -> new RuntimeException("Post not found"));
        }

        NotificationEntity entity = notificationPersistenceMapper.toEntity(notification, recipientEntity, actorEntity, postEntity);
        return notificationPersistenceMapper.toDomain(notificationJPARepository.save(entity));
    }

    @Override
    public void markAsRead(Long id, Long recipientId) {
        NotificationEntity entity = notificationJPARepository.findByIdAndRecipient_Id(id, recipientId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        entity.setRead(true);
        notificationJPARepository.save(entity);
    }

    @Override
    public void markAllAsRead(Long recipientId) {
        List<NotificationEntity> entities = notificationJPARepository.findByRecipient_IdOrderByTimestampDesc(recipientId);
        entities.forEach(entity -> entity.setRead(true));
        notificationJPARepository.saveAll(entities);
    }

    @Override
    public void deleteByPostId(Long postId) {
        notificationJPARepository.deleteByPost_Id(postId);
    }
}
