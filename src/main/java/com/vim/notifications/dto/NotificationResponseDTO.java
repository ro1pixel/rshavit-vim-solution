package com.vim.notifications.dto;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class NotificationResponseDTO {
    private String status;
    private String channel;
    private String to;
    private String message;
    private String error;

    public static NotificationResponseDTO success(String channel, String to, String message) {
        return NotificationResponseDTO.builder()
                .status("sent")
                .channel(channel)
                .to(to)
                .message(message)
                .build();
    }

    public static NotificationResponseDTO rateLimitError() {
        return NotificationResponseDTO.builder()
                .status("error")
                .error("Too many requests, please try again later.")
                .build();
    }

    public static NotificationResponseDTO serverError() {
        return NotificationResponseDTO.builder()
                .status("error")
                .error("Random server error occurred.")
                .build();
    }
}