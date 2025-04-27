package com.vim.notifications.repository.impl;

import com.vim.notifications.model.UserPreferences;
import com.vim.notifications.repository.UserPreferencesRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryUserPreferencesRepository implements UserPreferencesRepository {
    private final ConcurrentHashMap<Long, UserPreferences> userPreferencesById = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, UserPreferences> userPreferencesByEmail = new ConcurrentHashMap<>();
    private final AtomicLong userIdCounter = new AtomicLong(1);

    @Override
    public UserPreferences save(UserPreferences preferences) {
        Optional<UserPreferences> existingUser = findByUserIdOrEmail(
                Optional.ofNullable(preferences.getUserId()),
                Optional.ofNullable(preferences.getEmail()));

        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        preferences.setUserId(userIdCounter.getAndIncrement());
        userPreferencesById.put(preferences.getUserId(), preferences);
        userPreferencesByEmail.put(preferences.getEmail(), preferences);
        return preferences;
    }

    @Override
    public UserPreferences update(UserPreferences preferences) {
        Optional<UserPreferences> existingUser = findByUserIdOrEmail(
                Optional.ofNullable(preferences.getUserId()),
                Optional.ofNullable(preferences.getEmail()));

        if (!existingUser.isPresent()) {
            throw new IllegalArgumentException("User not found with userId: " +
                    preferences.getUserId() + " or email: " + preferences.getEmail());
        }

        UserPreferences existing = existingUser.get();
        preferences.setUserId(existing.getUserId()); // Preserve existing userId

        userPreferencesById.put(existing.getUserId(), preferences);
        userPreferencesByEmail.put(preferences.getEmail(), preferences);
        return preferences;
    }

    @Override
    public Optional<UserPreferences> findByUserIdOrEmail(Optional<Long> userId, Optional<String> email) {
        if (userId.isPresent()) {
            UserPreferences preferences = userPreferencesById.get(userId.get());
            if (preferences != null) {
                return Optional.of(preferences);
            }
        }

        if (email.isPresent()) {
            UserPreferences preferences = userPreferencesByEmail.get(email.get());
            if (preferences != null) {
                return Optional.of(preferences);
            }
        }

        return Optional.empty();
    }
}