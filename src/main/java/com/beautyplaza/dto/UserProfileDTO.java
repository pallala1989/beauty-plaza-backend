// dto/UserProfileDTO.java (NEW)
package com.beautyplaza.dto;

import lombok.Data;

@Data
public class UserProfileDTO {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    // You might add address, birthday, etc.
}