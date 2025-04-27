package com.vim.notifications.dto;

import lombok.Data;
import jakarta.validation.constraints.AssertTrue;

@Data
public class UserPreferencesDTO {
    private Long userId;
    private String email;
    private String telephone;
    private NotificationPreferences preferences;

    @AssertTrue(message = "Either userId or email must be provided")
    private boolean isUserIdOrEmailPresent() {
        return userId != null || (email != null && !email.isEmpty());
    }

    @Data
    public static class NotificationPreferences {
        private boolean email;
        private boolean sms;
    }
}