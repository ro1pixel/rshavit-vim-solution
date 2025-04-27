package com.vim.notifications.service.impl;

import com.vim.notifications.exception.NotificationException;
import com.vim.notifications.exception.NotificationInternalException;
import com.vim.notifications.exception.NotificationRateLimitException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import javax.management.RuntimeErrorException;

@Service
public class HttpNotificationServiceImpl {

    private final RestTemplate restTemplate;
    private final String notificationServiceUrl;

    public HttpNotificationServiceImpl(RestTemplate restTemplate, Environment env) {
        this.restTemplate = restTemplate;
        this.notificationServiceUrl = env.getProperty("NOTIFICATION_SERVICE_URL", "http://localhost:5001");

    }

    @Retryable(value = {
            NotificationRateLimitException.class }, maxAttempts = 5, backoff = @org.springframework.retry.annotation.Backoff(delay = 1000, multiplier = 2))
    public void sendNotification(String endpoint, Map<String, String> params) throws NotificationException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(params, headers);

            restTemplate.postForEntity(
                    notificationServiceUrl + endpoint,
                    request,
                    String.class);
        } catch (org.springframework.web.client.HttpStatusCodeException e) {
            if (e.getStatusCode().value() == 429) {
                throw new NotificationRateLimitException();
            }
            throw new NotificationInternalException();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}