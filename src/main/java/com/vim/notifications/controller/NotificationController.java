package com.vim.notifications.controller;

import com.vim.notifications.dto.NotificationRequestDTO;
import com.vim.notifications.service.impl.NotificationServiceImpl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationServiceImpl notificationService;

    public NotificationController(NotificationServiceImpl notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendNotification(@RequestBody NotificationRequestDTO request) {
        try {
            notificationService.sendNotification(request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}