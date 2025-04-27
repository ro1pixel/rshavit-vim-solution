package com.vim.notifications.dto;

import java.util.Optional;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationRequestDTO {
    private Optional<Long> userId;
    private Optional<String> email;
    private String message;
}