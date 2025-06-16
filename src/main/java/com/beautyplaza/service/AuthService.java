// service/AuthService.java (NEW)
package com.beautyplaza.service;

import com.beautyplaza.dto.AuthResponseDTO;
import com.beautyplaza.model.AuthUser;
import com.beautyplaza.repository.AuthUserRepository;
import com.beautyplaza.request.LoginRequest;
import com.beautyplaza.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponseDTO registerUser(RegisterRequest request) {
        // In a real app, perform more robust validation and error handling
        if (authUserRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (authUserRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        AuthUser newUser = new AuthUser();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword())); // Hash password!
        newUser.setEmail(request.getEmail());

        AuthUser savedUser = authUserRepository.save(newUser);

        // In a real app, generate a JWT token here
        AuthResponseDTO response = new AuthResponseDTO();
        response.setUserId(savedUser.getId());
        response.setUsername(savedUser.getUsername());
        response.setEmail(savedUser.getEmail());
        response.setToken("mock_jwt_token_for_" + savedUser.getUsername()); // Placeholder token
        return response;
    }

    public AuthResponseDTO loginUser(LoginRequest request) {
        Optional<AuthUser> userOptional = authUserRepository.findByUsername(request.getUsername());

        if (userOptional.isEmpty() || !passwordEncoder.matches(request.getPassword(), userOptional.get().getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        AuthUser user = userOptional.get();
        // In a real app, generate a JWT token here
        AuthResponseDTO response = new AuthResponseDTO();
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setToken("mock_jwt_token_for_" + user.getUsername()); // Placeholder token
        return response;
    }

    // You might add methods for password reset, email verification, etc.
}