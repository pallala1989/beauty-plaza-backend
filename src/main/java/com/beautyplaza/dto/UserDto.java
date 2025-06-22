package com.beautyplaza.dto;

// Importing Lombok annotations and Jakarta validation annotations.
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// @Getter and @Setter automatically generate getters and setters.
@Getter
@Setter
public class UserDto {
    private String id; // Unique identifier for the user.

    @NotBlank(message = "Email is mandatory") // Ensures the email field is not null and contains at least one non-whitespace character.
    @Email(message = "Email should be valid") // Ensures the email field is a well-formed email address.
    private String email; // User's email, used as a unique identifier for login.

    @NotBlank(message = "Password is mandatory") // Ensures the password field is not null and contains at least one non-whitespace character.
    @Size(min = 6, message = "Password must be at least 6 characters long") // Specifies minimum length for password.
    private String password; // User's password (will be hashed).

    @NotBlank(message = "Full name is mandatory") // Ensures the full name field is not null and contains at least one non-whitespace character.
    private String fullName; // User's full name.

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits") // Validates phone number to be exactly 10 digits.
    private String phone; // User's phone number.

    private String role; // Role of the user (e.g., 'admin', 'technician', 'user').

    private Boolean isActive; // Flag indicating if the user account is active.
}
