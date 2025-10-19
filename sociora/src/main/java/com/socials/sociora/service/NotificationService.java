package com.socials.sociora.service;

import com.socials.sociora.entity.Community;
import com.socials.sociora.entity.Notification;
import com.socials.sociora.entity.Post;
import com.socials.sociora.entity.User;
import com.socials.sociora.repository.NotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public Optional<Notification> getNotificationById(Long id) {
        return notificationRepository.findById(id);
    }

    public Page<Notification> getNotificationsForUser(User user, Pageable pageable) {
        return notificationRepository.findByUser(user, pageable);
    }

    public List<Notification> getUnreadNotificationsForUser(User user) {
        return notificationRepository.findByUserAndIsReadFalse(user);
    }

    public int getUnreadNotificationCount(User user) {
        return notificationRepository.countByUserAndIsReadFalse(user);
    }

    public List<Notification> getRecentNotificationsForUser(User user) {
        return notificationRepository.findTop10ByUserOrderByCreatedAtDesc(user);
    }

    @Transactional
    public Notification createNotification(User user, String type, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(type);
        notification.setMessage(message);
        
        return notificationRepository.save(notification);
    }

    @Transactional
    public Notification createNotificationWithRelatedUser(User user, User relatedUser, String type, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setRelatedUser(relatedUser);
        notification.setType(type);
        notification.setMessage(message);
        
        return notificationRepository.save(notification);
    }

    @Transactional
    public Notification createNotificationWithPost(User user, Post post, String type, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setPost(post);
        notification.setType(type);
        notification.setMessage(message);
        
        return notificationRepository.save(notification);
    }

    @Transactional
    public Notification createNotificationWithCommunity(User user, Community community, String type, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setCommunity(community);
        notification.setType(type);
        notification.setMessage(message);
        
        return notificationRepository.save(notification);
    }

    @Transactional
    public Notification markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        notification.setIsRead(true);
        return notificationRepository.save(notification);
    }

    @Transactional
    public List<Notification> markAllAsRead(User user) {
        List<Notification> unreadNotifications = notificationRepository.findByUserAndIsReadFalse(user);
        unreadNotifications.forEach(notification -> notification.setIsRead(true));
        return notificationRepository.saveAll(unreadNotifications);
    }

    @Transactional
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }
}