package com.beautyplaza.controller;


// Importing necessary Spring Framework, DTO classes.
import com.beautyplaza.dto.SettingDTO;
import com.beautyplaza.service.SettingsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST Controller for managing application Settings.
 * Provides endpoints for CRUD operations on settings, accessible by ADMIN only.
 */
@RestController // Marks this class as a REST controller.
@RequestMapping("/api/settings") // Base path for all endpoints in this controller.
public class SettingsController {

    @Autowired // Injects SettingsService for business logic operations on settings.
    private SettingsService settingsService;

    /**
     * Creates a new application setting. Accessible by ADMIN only.
     * @param settingsDto The SettingsDto containing setting key and value.
     * @return ResponseEntity with the created SettingsDto.
     */
    @PreAuthorize("hasRole('ADMIN')") // Only users with ADMIN role can access this.
    @PostMapping
    public ResponseEntity<SettingDTO> createSetting(@Valid @RequestBody SettingDTO settingsDto) {
        SettingDTO createdSetting = settingsService.createSetting(settingsDto);
        return new ResponseEntity<>(createdSetting, HttpStatus.CREATED); // Return 201 Created.
    }

    /**
     * Retrieves an application setting by its key. Accessible by ADMIN only.
     * @param key The key of the setting to retrieve.
     * @return ResponseEntity with the SettingsDto.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{key}")
    public ResponseEntity<SettingDTO> getSettingByKey(@PathVariable String key) {
        SettingDTO setting = settingsService.getSettingByKey(key);
        return ResponseEntity.ok(setting); // Return 200 OK.
    }

    /**
     * Retrieves all application settings. Accessible by ADMIN only.
     * @return ResponseEntity with a list of SettingsDtos.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<SettingDTO>> getAllSettings() {
        List<SettingDTO> settings = settingsService.getAllSettings();
        return ResponseEntity.ok(settings); // Return 200 OK.
    }

    /**
     * Updates an existing application setting. Accessible by ADMIN only.
     * @param key The key of the setting to update.
     * @param settingsDto The SettingsDto containing the updated setting value.
     * @return ResponseEntity with the updated SettingsDto.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{key}")
    public ResponseEntity<SettingDTO> updateSetting(@PathVariable String key, @Valid @RequestBody SettingDTO settingsDto) {
        SettingDTO updatedSetting = settingsService.updateSetting(key, settingsDto);
        return ResponseEntity.ok(updatedSetting); // Return 200 OK.
    }

    /**
     * Deletes an application setting by its key. Accessible by ADMIN only.
     * @param key The key of the setting to delete.
     * @return ResponseEntity with no content.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{key}")
    public ResponseEntity<Void> deleteSetting(@PathVariable String key) {
        settingsService.deleteSetting(key);
        return ResponseEntity.noContent().build(); // Return 204 No Content.
    }
}
