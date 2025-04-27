package com.vim.notifications.controller;

import com.vim.notifications.dto.NotificationRequestDTO;
import com.vim.notifications.dto.NotificationResponseDTO;
import com.vim.notifications.service.impl.NotificationServiceImpl;
import com.vim.notifications.exception.NotificationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/api/v1/notifications")
@Validated
public class NotificationController {

    private final NotificationServiceImpl notificationService;

    public NotificationController(NotificationServiceImpl notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendNotification(
            @Validated @RequestBody NotificationRequestDTO request) {
        try {
            notificationService.sendNotification(request);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NotificationException e) {
            if (e.getErrorType() == NotificationException.NotificationErrorType.RATE_LIMIT) {
                return ResponseEntity.status(429).body(e.getMessage());
            }
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}