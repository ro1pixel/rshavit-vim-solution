package com.vim.notifications.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Retryable;
import org.springframework.core.env.Environment;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.scheduling.annotation.Async;
import com.vim.notifications.dto.NotificationRequestDTO;
import com.vim.notifications.service.NotificationService;
import com.vim.notifications.model.UserPreferences;
import com.vim.notifications.model.NotificationType;
import com.vim.notifications.repository.UserPreferencesRepository;
import com.vim.notifications.repository.RepositoryType;
import com.vim.notifications.repository.UserPreferencesRepository.Factory;
import com.vim.notifications.exception.NotificationException;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final UserPreferencesRepository userPreferencesRepository;
    private final HttpNotificationServiceImpl httpNotificationServiceImpl;

    public NotificationServiceImpl(RestTemplate restTemplate, Factory repositoryFactory,
            HttpNotificationServiceImpl httpNotificationServiceImpl) {
        this.httpNotificationServiceImpl = httpNotificationServiceImpl;
        this.userPreferencesRepository = repositoryFactory.ofType(RepositoryType.Cache);
    }

    public CompletableFuture<Void> sendNotification(NotificationRequestDTO request) {
        if (request.getMessage() == null) {
            throw new IllegalArgumentException("Message is required");
        }

        UserPreferences user = userPreferencesRepository.findByUserIdOrEmail(request.getUserId(), request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found with provided ID or email"));

        Map<String, Boolean> preferences = user.getPreferences();

        for (NotificationType notificationType : NotificationType.values()) {
            if (preferences.getOrDefault(notificationType.getPreferenceKey(), false)) {
                String contact = notificationType.getContact(user);
                if (contact != null) {
                    notificationType.send(this, contact, request.getMessage());
                }
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    @Async("notificationTaskExecutor")
    public CompletableFuture<Void> sendEmailNotification(String email, String message) {
        Map<String, String> params = Map.of(
                "email", email,
                "message", message);

        httpNotificationServiceImpl.sendNotification("/send-email", params);
        return CompletableFuture.completedFuture(null);
    }

    @Async("notificationTaskExecutor")
    public CompletableFuture<Void> sendSmsNotification(String telephone, String message) {
        Map<String, String> params = Map.of(
                "telephone", telephone,
                "message", message);

        httpNotificationServiceImpl.sendNotification("/send-sms", params);
        return CompletableFuture.completedFuture(null);
    }
}