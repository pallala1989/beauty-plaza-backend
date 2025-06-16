// request/UpdateProfileRequest.java (NEW)
package com.beautyplaza.request;

import lombok.Data;
import jakarta.validation.constraints.Email;

@Data
public class UpdateProfileRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    @Email
    private String email;
    // Add other updatable profile fields
}