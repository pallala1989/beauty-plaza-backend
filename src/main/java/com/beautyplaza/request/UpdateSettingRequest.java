// request/UpdateSettingRequest.java (NEW)
package com.beautyplaza.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateSettingRequest {
    @NotBlank
    private String settingKey;
    private String settingValue; // Can be null if removing
    private String description;
    private String category;
}