package com.vim.notifications.repository;

import com.vim.notifications.model.UserPreferences;
import com.vim.notifications.repository.impl.InMemoryUserPreferencesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

public interface UserPreferencesRepository {
    UserPreferences save(UserPreferences preferences);

    UserPreferences update(UserPreferences preferences);

    Optional<UserPreferences> findByUserIdOrEmail(Long userId, String email);

    @Component
    class Factory {
        @Autowired
        private InMemoryUserPreferencesRepository cacheRepository;

        public UserPreferencesRepository ofType(RepositoryType type) {
            switch (type) {
                case Cache:
                    return cacheRepository;
                case Database:
                    throw new UnsupportedOperationException("Database repository not implemented yet");
                default:
                    throw new IllegalArgumentException("Unknown repository type: " + type);
            }
        }
    }
}