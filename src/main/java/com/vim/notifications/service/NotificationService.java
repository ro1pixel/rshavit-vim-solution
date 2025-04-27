package com.vim.notifications.service;

import com.vim.notifications.dto.NotificationRequestDTO;
import java.util.concurrent.CompletableFuture;

public interface NotificationService {
    CompletableFuture<Void> sendNotification(NotificationRequestDTO request);
}