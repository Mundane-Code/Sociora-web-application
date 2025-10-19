package com.socials.sociora.repository;

import com.socials.sociora.entity.Notification;
import com.socials.sociora.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByUser(User user, Pageable pageable);
    List<Notification> findByUserAndIsReadFalse(User user);
    int countByUserAndIsReadFalse(User user);
    List<Notification> findTop10ByUserOrderByCreatedAtDesc(User user);
}