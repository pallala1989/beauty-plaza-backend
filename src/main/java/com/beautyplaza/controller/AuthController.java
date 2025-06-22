package com.beautyplaza.controller;

import org.springframework.web.bind.annotation.*;

// Importing necessary Spring Framework and DTO classes.
import com.beautyplaza.dto.*;

import com.beautyplaza.model.User;
import com.beautyplaza.exception.ApiException;
import com.beautyplaza.repository.UserRepository;
import com.beautyplaza.security.CustomUserDetailsService;
import com.beautyplaza.security.JwtHelper;
import com.beautyplaza.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for authentication related endpoints (login and registration).
 * Handles user login and new user registration.
 */
@RestController // Marks this class as a REST controller.
@RequestMapping("/auth") // Base path for all endpoints in this controller.
public class AuthController {

    @Autowired // Injects CustomUserDetailsService to load user details for authentication.
    private CustomUserDetailsService userDetailsService;

    @Autowired // Injects AuthenticationManager to authenticate user credentials.
    private AuthenticationManager manager;

    @Autowired // Injects JwtHelper for generating JWT tokens.
    private JwtHelper helper;

    @Autowired // Injects UserRepository to fetch user details (e.g., user role).
    private UserRepository userRepository;

    @Autowired // Injects UserService for user registration.
    private UserService userService;

    /**
     * Handles user login requests.
     * Authenticates the user with provided credentials and generates a JWT token upon successful authentication.
     * @param request The LoginRequest DTO containing user email and password.
     * @return ResponseEntity with AuthResponse containing JWT token, user ID, and role, or an error.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        this.doAuthenticate(request.getEmail(), request.getPassword()); // Authenticate credentials.

        // Load user details to retrieve user ID and role for the AuthResponse.
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.helper.generateToken(userDetails); // Generate JWT token.

        // Retrieve the User entity to get the actual ID (UUID).
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "User not found after successful authentication."));

        // Build the authentication response.
        AuthResponse response = AuthResponse.builder()
                .token(token)
                .userId(user.getId()) // Set actual user ID.
                .role(user.getRole().name()) // Set user's role.
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK); // Return 200 OK with the token.
    }

    /**
     * Performs authentication using Spring Security's AuthenticationManager.
     * @param email The user's email.
     * @param password The user's password.
     * @throws ApiException if authentication fails (e.g., bad credentials).
     */
    private void doAuthenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            manager.authenticate(authentication); // Attempt authentication.
        } catch (BadCredentialsException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid Username or Password !!");
        }
    }

    /**
     * Handles new user registration requests.
     * Creates a new user account with the provided details.
     * @param userDto The UserDto containing details for the new user.
     * @return ResponseEntity with the created UserDto, or an error.
     */
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody UserDto userDto) {
        UserDto registeredUser = userService.createUser(userDto); // Delegate to UserService for creation.
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED); // Return 201 Created.
    }
}
