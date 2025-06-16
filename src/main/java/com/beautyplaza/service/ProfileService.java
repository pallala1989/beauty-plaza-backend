// service/ProfileService.java (NEW)
package com.beautyplaza.service;

import com.beautyplaza.dto.UserProfileDTO;
import com.beautyplaza.model.AuthUser;
import com.beautyplaza.repository.AuthUserRepository;
import com.beautyplaza.request.PasswordChangeRequest;
import com.beautyplaza.request.UpdateProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;

    public UserProfileDTO getUserProfile(Long userId) {
        AuthUser user = authUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        return toUserProfileDTO(user);
    }

    public UserProfileDTO updateProfile(Long userId, UpdateProfileRequest request) {
        AuthUser user = authUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (authUserRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new RuntimeException("Email already taken.");
            }
            user.setEmail(request.getEmail());
        }
        // You'd need to add fields to AuthUser or a separate UserProfile entity
        // For this example, assuming firstName, lastName, phoneNumber are part of a broader profile concept
        // user.setFirstName(request.getFirstName());
        // user.setLastName(request.getLastName());
        // user.setPhoneNumber(request.getPhoneNumber());

        AuthUser updatedUser = authUserRepository.save(user);
        return toUserProfileDTO(updatedUser);
    }

    public void changePassword(Long userId, PasswordChangeRequest request) {
        AuthUser user = authUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password does not match.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        authUserRepository.save(user);
    }


    private UserProfileDTO toUserProfileDTO(AuthUser user) {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        // Map other profile fields if they exist in AuthUser or a separate profile entity
        return dto;
    }
}