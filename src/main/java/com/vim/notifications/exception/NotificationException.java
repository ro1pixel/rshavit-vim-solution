package com.vim.notifications.exception;

public class NotificationException extends RuntimeException {
    private final NotificationErrorType errorType;

    public NotificationException(NotificationErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public NotificationErrorType getErrorType() {
        return errorType;
    }

    public enum NotificationErrorType {
        RATE_LIMIT("Too many requests, please try again later."),
        SERVER_ERROR("Unknown server error occurred.");

        private final String message;

        NotificationErrorType(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}