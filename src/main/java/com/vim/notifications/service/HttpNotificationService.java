package com.vim.notifications.service;

import com.vim.notifications.exception.NotificationException;
import java.util.Map;

public interface HttpNotificationService {
    void sendNotification(String endpoint, Map<String, String> params) throws NotificationException;
}