// dto/SettingDTO.java (NEW)
package com.beautyplaza.dto;

import lombok.Data;

@Data
public class SettingDTO {
    private Long id;
    private String key;
    private String value;
    private String description;
    private String category;
}