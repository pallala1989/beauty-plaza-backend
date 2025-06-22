package com.beautyplaza.dto;

import lombok.Getter;
import lombok.Setter;

// @Getter and @Setter are Lombok annotations to automatically generate
// getter and setter methods for all fields in the class.
@Getter
@Setter
public class LoginRequest {
    private String email;    // Field to hold the user's email.
    private String password; // Field to hold the user's password.
}