package com.vim.notifications.model;

import com.vim.notifications.service.impl.NotificationServiceImpl;
import com.vim.notifications.dto.NotificationRequestDTO;

public enum NotificationType {
    EMAIL("email") {
        @Override
        public void send(NotificationServiceImpl service, String contact, String message) {
            service.sendEmailNotification(contact, message);
        }

        @Override
        public String getContact(UserPreferences user) {
            return user.getEmail();
        }
    },
    SMS("sms") {
        @Override
        public void send(NotificationServiceImpl service, String contact, String message) {
            service.sendSmsNotification(contact, message);
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

    public abstract void send(NotificationServiceImpl service, String contact, String message);

    public abstract String getContact(UserPreferences user);
}