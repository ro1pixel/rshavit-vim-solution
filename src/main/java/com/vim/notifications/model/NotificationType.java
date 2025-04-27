package com.vim.notifications.model;

import com.vim.notifications.service.impl.NotificationServiceImpl;
import java.util.concurrent.CompletableFuture;

public enum NotificationType {
    EMAIL("email") {
        @Override
        public CompletableFuture<Void> send(NotificationServiceImpl service, String contact, String message) {
            return service.sendEmailNotification(contact, message);
        }

        @Override
        public String getContact(UserPreferences user) {
            return user.getEmail();
        }
    },
    SMS("sms") {
        @Override
        public CompletableFuture<Void> send(NotificationServiceImpl service, String contact, String message) {
            return service.sendSmsNotification(contact, message);
        }

        @Override
        public String getContact(UserPreferences user) {
            return user.getTelephone();
        }
    };

    private final String preferenceKey;

    NotificationType(String preferenceKey) {
        this.preferenceKey = preferenceKey;
    }

    public String getPreferenceKey() {
        return preferenceKey;
    }

    public abstract CompletableFuture<Void> send(NotificationServiceImpl service, String contact, String message);

    public abstract String getContact(UserPreferences user);
}