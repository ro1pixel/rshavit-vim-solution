package com.vim.notifications.exception;

public class NotificationInternalException extends NotificationException {
    public NotificationInternalException() {
        super(NotificationErrorType.SERVER_ERROR);
    }
}