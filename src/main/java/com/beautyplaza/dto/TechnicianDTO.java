// dto/TechnicianDTO.java
package com.beautyplaza.dto;

import lombok.Data;
import java.util.List;

@Data
public class TechnicianDTO {
    private Long id;
    private String name;
    private List<String> specialties;
}