// service/SettingsService.java (NEW - replaces AdminController logic)
package com.beautyplaza.service;


// Importing the SettingsDto.
import com.beautyplaza.dto.SettingDTO;
import java.util.List;

/**
 * Interface for Settings-related business logic.
 * Defines the contract for operations on application settings.
 */
public interface SettingsService {
    /**
     * Creates a new application setting.
     * @param settingsDto The SettingsDto containing setting details.
     * @return The created SettingsDto.
     */
    SettingDTO createSetting(SettingDTO settingsDto);

    /**
     * Retrieves a setting by its unique key.
     * @param settingKey The key of the setting to retrieve.
     * @return The SettingsDto of the found setting.
     */
    SettingDTO getSettingByKey(String settingKey);

    /**
     * Retrieves all application settings.
     * @return A list of all SettingsDtos.
     */
    List<SettingDTO> getAllSettings();

    /**
     * Updates an existing application setting.
     * @param settingKey The key of the setting to update.
     * @param settingsDto The SettingsDto containing updated details.
     * @return The updated SettingsDto.
     */
    SettingDTO updateSetting(String settingKey, SettingDTO settingsDto);

    /**
     * Deletes an application setting by its key.
     * @param settingKey The key of the setting to delete.
     */
    void deleteSetting(String settingKey);
}
