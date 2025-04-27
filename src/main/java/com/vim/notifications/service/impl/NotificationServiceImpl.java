package com.vim.notifications.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.vim.notifications.dto.NotificationRequestDTO;
import com.vim.notifications.service.NotificationService;
import com.vim.notifications.model.UserPreferences;
import com.vim.notifications.model.NotificationType;
import com.vim.notifications.repository.UserPreferencesRepository;
import com.vim.notifications.repository.RepositoryType;
import com.vim.notifications.repository.UserPreferencesRepository.Factory;
import com.vim.notifications.exception.NotificationException;

import java.util.Map;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final RestTemplate restTemplate;
    private final UserPreferencesRepository userPreferencesRepository;
    private final String notificationServiceUrl;

    public NotificationServiceImpl(RestTemplate restTemplate, Factory repositoryFactory, Environment env) {
        this.restTemplate = restTemplate;
        this.userPreferencesRepository = repositoryFactory.ofType(RepositoryType.Cache);
        this.notificationServiceUrl = env.getProperty("NOTIFICATION_SERVICE_URL", "http://localhost:5001");
    }

    public void sendNotification(NotificationRequestDTO request) {
        if (request.getMessage() == null) {
            throw new IllegalArgumentException("Message is required");
        }

        UserPreferences user = userPreferencesRepository.findByUserIdOrEmail(request.getUserId(), request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + request.getUserId()));

        Map<String, Boolean> preferences = user.getPreferences();

        for (NotificationType notificationType : NotificationType.values()) {
            if (preferences.getOrDefault(notificationType.getPreferenceKey(), false)) {
                String contact = notificationType.getContact(user);
                if (contact != null) {
                    notificationType.send(this, contact, request.getMessage());
                }
            }
        }
    }

    public void sendEmailNotification(String email, String message) {
        Map<String, String> emailRequest = Map.of(
                "email", email,
                "message", message);

        HttpEntity<Map<String, String>> request = createHttpEntity(emailRequest);
        try {
            restTemplate.postForObject(notificationServiceUrl + "/send-email", request, String.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 429) {
                throw new NotificationException(NotificationException.NotificationErrorType.RATE_LIMIT);
            }
            throw new NotificationException(NotificationException.NotificationErrorType.SERVER_ERROR);
        } catch (Exception e) {
            throw new NotificationException(NotificationException.NotificationErrorType.SERVER_ERROR);
        }
    }

    public void sendSmsNotification(String telephone, String message) {
        Map<String, String> smsRequest = Map.of(
                "telephone", telephone,
                "message", message);

        HttpEntity<Map<String, String>> request = createHttpEntity(smsRequest);
        try {
            restTemplate.postForObject(notificationServiceUrl + "/send-sms", request, String.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 429) {
                throw new NotificationException(NotificationException.NotificationErrorType.RATE_LIMIT);
            }
            throw new NotificationException(NotificationException.NotificationErrorType.SERVER_ERROR);
        } catch (Exception e) {
            throw new NotificationException(NotificationException.NotificationErrorType.SERVER_ERROR);
        }
    }

    private <T> HttpEntity<T> createHttpEntity(T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }
}