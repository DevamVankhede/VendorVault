package com.orion.vendorvault.controller;

import com.orion.vendorvault.service.NotificationService;
import com.orion.vendorvault.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public String viewNotifications(Model model) {
        Long userId = SecurityUtil.getCurrentUserId();
        model.addAttribute("notifications", notificationService.getNotificationsForUser(userId, PageRequest.of(0, 50)));
        return "notification/list";
    }

    @PostMapping("/{id}/read")
    public String markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return "redirect:/notifications";
    }

    @PostMapping("/read-all")
    public String markAllAsRead() {
        notificationService.markAllAsRead(SecurityUtil.getCurrentUserId());
        return "redirect:/notifications";
    }
}
