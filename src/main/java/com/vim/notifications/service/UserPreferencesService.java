package com.vim.notifications.service;

import com.vim.notifications.dto.UserPreferencesDTO;

public interface UserPreferencesService {
    UserPreferencesDTO createUserPreferences(UserPreferencesDTO userPreferences);

    UserPreferencesDTO updateUserPreferences(UserPreferencesDTO userPreferences);
}