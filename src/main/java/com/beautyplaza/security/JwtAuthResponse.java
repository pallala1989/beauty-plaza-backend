package com.beautyplaza.security;

// Importing Lombok annotations.
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the response sent back to the client after successful authentication
 * with JWT. Contains the JWT token and a message.
 */
@Getter
@Setter
public class JwtAuthResponse {
    private String token;      // The actual JWT token.
    private String message = "Login Success !!"; // A success message.
}
