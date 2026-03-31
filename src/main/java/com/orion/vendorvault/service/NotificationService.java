package com.orion.vendorvault.service;

import com.orion.vendorvault.model.entity.Notification;
import com.orion.vendorvault.model.enums.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    void sendNotification(Long userId, NotificationType type, String title, String message, String actionUrl);
    Page<Notification> getNotificationsForUser(Long userId, Pageable pageable);
    void markAsRead(Long id);
    void markAllAsRead(Long userId);
    long getUnreadCount(Long userId);
}
