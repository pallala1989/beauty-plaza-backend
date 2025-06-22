package com.beautyplaza.dto;

// Importing Lombok annotations.
import lombok.AllArgsConstructor;
import lombok.Builder; // Import the Builder annotation
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// @Getter and @Setter automatically generate getters and setters.
// @NoArgsConstructor generates a constructor with no arguments.
// @AllArgsConstructor generates a constructor with arguments for all fields.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Add @Builder to enable the builder pattern
public class AuthResponse {
    private String token; // Field to hold the JWT token.
    private String userId; // Field to hold the user's ID.
    private String role; // Field to hold the user's role.
}
