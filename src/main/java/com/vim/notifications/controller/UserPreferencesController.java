package com.vim.notifications.controller;

import com.vim.notifications.dto.UserPreferencesDTO;
import com.vim.notifications.service.UserPreferencesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserPreferencesController {

    private final UserPreferencesService userPreferencesService;

    public UserPreferencesController(UserPreferencesService userPreferencesService) {
        this.userPreferencesService = userPreferencesService;
    }

    @PostMapping
    public ResponseEntity<UserPreferencesDTO> createUserPreferences(
            @RequestBody UserPreferencesDTO userPreferences) {
        return new ResponseEntity<>(userPreferencesService.createUserPreferences(userPreferences), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<UserPreferencesDTO> updateUserPreferences(
            @RequestBody UserPreferencesDTO userPreferences) {
        return ResponseEntity.ok(userPreferencesService.updateUserPreferences(userPreferences));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}