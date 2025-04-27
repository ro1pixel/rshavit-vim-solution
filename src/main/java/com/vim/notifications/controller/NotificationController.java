package com.vim.notifications.controller;

import com.vim.notifications.dto.NotificationRequestDTO;
import com.vim.notifications.service.NotificationService;
import com.vim.notifications.exception.NotificationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/api/v1/notifications")
@Validated
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendNotification(
            @Validated @RequestBody NotificationRequestDTO request) {
        try {
            notificationService.sendNotification(request)
                    .exceptionally(throwable -> {
                        if (throwable instanceof NotificationException) {
                            NotificationException e = (NotificationException) throwable;
                            if (e.getErrorType() == NotificationException.NotificationErrorType.RATE_LIMIT) {
                                throw new NotificationException(NotificationException.NotificationErrorType.RATE_LIMIT);
                            }
                        }
                        throw new NotificationException(NotificationException.NotificationErrorType.SERVER_ERROR);
                    });
            return ResponseEntity.accepted().build();
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