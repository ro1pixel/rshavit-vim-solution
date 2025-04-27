package com.vim.notifications.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class NotificationRequestDTO {
    private Long userId;
    private String email;

    @NotBlank(message = "Message is required")
    private String message;

    @AssertTrue(message = "Either userId or email must be provided")
    private boolean isUserIdOrEmailPresent() {
        return userId != null || (email != null && !email.isEmpty());
    }
}