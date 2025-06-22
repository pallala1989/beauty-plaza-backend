package com.beautyplaza.service.impl;

// Importing necessary classes for service logic.
import com.beautyplaza.dto.SettingDTO;
import com.beautyplaza.model.Setting;
import com.beautyplaza.exception.ResourceNotFoundException;
import com.beautyplaza.repository.SettingRepository;
import com.beautyplaza.service.SettingsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the SettingsService interface.
 * Handles business logic related to application settings.
 */
@Service // Marks this class as a Spring Service component.
public class SettingsServiceImpl implements SettingsService {

    @Autowired // Injects SettingsRepository for database interaction.
    private SettingRepository settingsRepository;

    @Autowired // Injects ModelMapper for object mapping (Entity <-> DTO).
    private ModelMapper modelMapper;

    /**
     * Creates a new application setting.
     * @param settingsDto The SettingsDto containing setting details.
     * @return The created SettingsDto.
     */
    @Override
    public SettingDTO createSetting(SettingDTO settingsDto) {
        // Map DTO to Entity.
        Setting settings = modelMapper.map(settingsDto, Setting.class);
        // Save the new setting to the database.
        Setting savedSetting = settingsRepository.save(settings);
        // Map the saved Entity back to DTO and return.
        return modelMapper.map(savedSetting, SettingDTO.class);
    }

    /**
     * Retrieves a setting by its unique key.
     * @param settingKey The key of the setting to retrieve.
     * @return The SettingsDto of the found setting.
     * @throws ResourceNotFoundException if no setting is found with the given key.
     */
    @Override
    public SettingDTO getSettingByKey(String settingKey) {
        Setting settings = settingsRepository.findById(settingKey)
                .orElseThrow(() -> new ResourceNotFoundException("Setting", "key", settingKey));
        return modelMapper.map(settings, SettingDTO.class);
    }

    /**
     * Retrieves a list of all application settings.
     * @return A list of SettingsDto objects.
     */
    @Override
    public List<SettingDTO> getAllSettings() {
        List<Setting> settings = settingsRepository.findAll();
        // Stream through the list of Settings entities, map each to a SettingsDto, and collect into a new list.
        return settings.stream().map(setting -> modelMapper.map(setting, SettingDTO.class)).collect(Collectors.toList());
    }

    /**
     * Updates an existing application setting.
     * @param settingKey The key of the setting to update.
     * @param settingsDto The SettingsDto containing the updated setting value.
     * @return The updated SettingsDto.
     * @throws ResourceNotFoundException if no setting is found with the given key.
     */
    @Override
    public SettingDTO updateSetting(String settingKey, SettingDTO settingsDto) {
        Setting existingSetting = settingsRepository.findById(settingKey)
                .orElseThrow(() -> new ResourceNotFoundException("Setting", "key", settingKey));

        // Update the setting value if provided.
      //  Optional.ofNullable(settingsDto.getValue()).ifPresent(existingSetting::setValue);

        Setting updatedSetting = settingsRepository.save(existingSetting);
        return modelMapper.map(updatedSetting, SettingDTO.class);
    }

    /**
     * Deletes an application setting by its key.
     * @param settingKey The key of the setting to delete.
     * @throws ResourceNotFoundException if no setting is found with the given key.
     */
    @Override
    public void deleteSetting(String settingKey) {
        Setting settings = settingsRepository.findById(settingKey)
                .orElseThrow(() -> new ResourceNotFoundException("Setting", "key", settingKey));
        settingsRepository.delete(settings);
    }
}
