package org.example.fitpinserver.DAL.repositories;

import org.example.fitpinserver.DAL.entities.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationJPARepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findByRecipient_IdOrderByTimestampDesc(Long recipientId);
    long countByRecipient_IdAndReadFalse(Long recipientId);
    Optional<NotificationEntity> findByIdAndRecipient_Id(Long id, Long recipientId);
}
