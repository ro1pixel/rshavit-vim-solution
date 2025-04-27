package com.vim.notifications.exception;

public class NotificationRateLimitException extends NotificationException {
    public NotificationRateLimitException() {
        super(NotificationErrorType.RATE_LIMIT);
    }
}