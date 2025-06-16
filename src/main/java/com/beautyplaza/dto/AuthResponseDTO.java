// dto/AuthResponseDTO.java (NEW)
package com.beautyplaza.dto;

import lombok.Data;

@Data
public class AuthResponseDTO {
    private String token; // e.g., JWT
    private Long userId;
    private String username;
    private String email;
    // You might add roles here as well
}