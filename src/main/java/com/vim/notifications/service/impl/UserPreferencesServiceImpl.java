package com.vim.notifications.service.impl;

import com.vim.notifications.repository.UserPreferencesRepository;
import com.vim.notifications.dto.UserPreferencesDTO;
import com.vim.notifications.model.UserPreferences;
import com.vim.notifications.service.UserPreferencesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserPreferencesServiceImpl implements UserPreferencesService {
    private final UserPreferencesRepository userPreferencesRepository;
    private final ObjectMapper objectMapper;

    public UserPreferencesServiceImpl(UserPreferencesRepository userPreferencesRepository, ObjectMapper objectMapper) {
        this.userPreferencesRepository = userPreferencesRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public UserPreferencesDTO createUserPreferences(UserPreferencesDTO userPreferences) {
        UserPreferences preferences = convertToEntity(userPreferences);
        UserPreferences savedPreferences = userPreferencesRepository.save(preferences);
        return convertToDTO(savedPreferences);
    }

    @Override
    public UserPreferencesDTO updateUserPreferences(UserPreferencesDTO userPreferences) {
        Optional<UserPreferences> existing = userPreferencesRepository.findByUserIdOrEmail(
                Optional.ofNullable(userPreferences.getUserId()), Optional.ofNullable(userPreferences.getEmail()));

        if (existing.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        UserPreferences preferences = convertToEntity(userPreferences);
        UserPreferences updatedPreferences = userPreferencesRepository.update(preferences);
        return convertToDTO(updatedPreferences);
    }

    private UserPreferences convertToEntity(UserPreferencesDTO dto) {
        UserPreferences entity = new UserPreferences();
        entity.setUserId(dto.getUserId());
        entity.setEmail(dto.getEmail());
        entity.setTelephone(dto.getTelephone());

        Map<String, Boolean> preferencesMap = new HashMap<>();
        if (dto.getPreferences() != null) {
            preferencesMap.put("email", dto.getPreferences().isEmail());
            preferencesMap.put("sms", dto.getPreferences().isSms());
        }
        entity.setPreferences(preferencesMap);
        return entity;
    }

    private UserPreferencesDTO convertToDTO(UserPreferences entity) {
        UserPreferencesDTO dto = new UserPreferencesDTO();
        dto.setUserId(entity.getUserId());
        dto.setEmail(entity.getEmail());
        dto.setTelephone(entity.getTelephone());

        UserPreferencesDTO.NotificationPreferences notificationPreferences = new UserPreferencesDTO.NotificationPreferences();
        if (entity.getPreferences() != null) {
            notificationPreferences.setEmail(entity.getPreferences().getOrDefault("email", false));
            notificationPreferences.setSms(entity.getPreferences().getOrDefault("sms", false));
        }
        dto.setPreferences(notificationPreferences);
        return dto;
    }
}