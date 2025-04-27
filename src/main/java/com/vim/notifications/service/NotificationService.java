package com.vim.notifications.service;

import com.vim.notifications.dto.NotificationRequestDTO;

public interface NotificationService {
    void sendNotification(NotificationRequestDTO request);
}