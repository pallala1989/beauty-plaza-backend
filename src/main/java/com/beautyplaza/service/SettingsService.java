// service/SettingsService.java (NEW - replaces AdminController logic)
package com.beautyplaza.service;

import com.beautyplaza.dto.SettingDTO;
import com.beautyplaza.model.Setting;
import com.beautyplaza.repository.SettingRepository;
import com.beautyplaza.request.UpdateSettingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SettingsService {

    private final SettingRepository settingRepository;

    public Optional<SettingDTO> getSetting(String key) {
        return settingRepository.findBySettingKey(key)
                .map(this::toSettingDTO);
    }

    public List<SettingDTO> getAllSettings() {
        return settingRepository.findAll().stream()
                .map(this::toSettingDTO)
                .collect(Collectors.toList());
    }

    public List<SettingDTO> getSettingsByCategory(String category) {
        return settingRepository.findByCategory(category).stream()
                .map(this::toSettingDTO)
                .collect(Collectors.toList());
    }

    public SettingDTO createOrUpdateSetting(UpdateSettingRequest request) {
        Setting setting = settingRepository.findBySettingKey(request.getSettingKey())
                .orElse(new Setting()); // Create new if not found

        setting.setSettingKey(request.getSettingKey());
        setting.setSettingValue(request.getSettingValue());
        setting.setDescription(request.getDescription() != null ? request.getDescription() : setting.getDescription());
        setting.setCategory(request.getCategory() != null ? request.getCategory() : setting.getCategory());

        return toSettingDTO(settingRepository.save(setting));
    }

    public void deleteSetting(String key) {
        Setting setting = settingRepository.findBySettingKey(key)
                .orElseThrow(() -> new RuntimeException("Setting not found with key: " + key));
        settingRepository.delete(setting);
    }

    private SettingDTO toSettingDTO(Setting setting) {
        SettingDTO dto = new SettingDTO();
        dto.setId(setting.getId());
        dto.setKey(setting.getSettingKey());
        dto.setValue(setting.getSettingValue());
        dto.setDescription(setting.getDescription());
        dto.setCategory(setting.getCategory());
        return dto;
    }
}