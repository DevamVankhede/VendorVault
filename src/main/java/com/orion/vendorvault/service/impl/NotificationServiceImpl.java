package com.orion.vendorvault.service.impl;

import com.orion.vendorvault.model.entity.Notification;
import com.orion.vendorvault.model.entity.User;
import com.orion.vendorvault.model.enums.NotificationType;
import com.orion.vendorvault.repository.NotificationRepository;
import com.orion.vendorvault.repository.UserRepository;
import com.orion.vendorvault.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Override
    @Transactional
    @CacheEvict(value = "unread-count", key = "#userId")
    public void sendNotification(Long userId, NotificationType type, String title, String message, String actionUrl) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        Notification notification = Notification.builder()
                .user(user)
                .type(type)
                .title(title)
                .message(message)
                .relatedEntityType("URL") // Or map appropriately
                .relatedEntityId(0L) // Map appropriately
                .read(false)
                .build();
                
        notificationRepository.save(notification);
        log.info("Saved notification for userId: {}", userId);
        
        dispatchEmailAsync(user.getEmail(), title, message, actionUrl, type.name());
    }

    @Async
    protected void dispatchEmailAsync(String to, String subject, String messageText, String actionUrl, String type) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            Context context = new Context();
            context.setVariable("message", messageText);
            context.setVariable("actionUrl", actionUrl);
            context.setVariable("title", subject);
            
            // Just use a generic template for now, will map to specific ones later
            String templateName = "email/generic-notification"; 
            if (type.contains("WELCOME")) templateName = "email/welcome-email";
            else if (type.contains("APPROVAL")) templateName = "email/approval-required";
            else if (type.contains("EXPIRE")) templateName = "email/expiry-alert";
            else if (type.contains("LOCKED")) templateName = "email/account-locked";
            else if (type.contains("REJECTED")) templateName = "email/contract-rejected";
            else if (type.contains("APPROVED")) templateName = "email/contract-approved";
            
            String htmlContent = templateEngine.process(templateName, context);
            
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            
            javaMailSender.send(message);
            log.info("Email dispatched asynchronously to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send email to {}", to, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Notification> getNotificationsForUser(Long userId, Pageable pageable) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    @Override
    @Transactional
    @CacheEvict(value = "unread-count", allEntries = true)
    public void markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id).orElseThrow();
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    @CacheEvict(value = "unread-count", key = "#userId")
    public void markAllAsRead(Long userId) {
        notificationRepository.markAllAsReadForUser(userId);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "unread-count", key = "#userId")
    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndReadFalse(userId);
    }
}
