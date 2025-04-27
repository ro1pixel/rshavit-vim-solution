package com.vim.notifications.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferences {
    private Long userId;
    private String email;
    private String telephone;
    private Map<String, Boolean> preferences;
}