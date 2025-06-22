
package com.beautyplaza.service.impl;

// Importing necessary classes for service logic.
import com.beautyplaza.dto.UserDto;
import com.beautyplaza.model.Role;
import com.beautyplaza.model.User;
import com.beautyplaza.exception.ApiException;
import com.beautyplaza.exception.ResourceNotFoundException;
import com.beautyplaza.repository.UserRepository;
import com.beautyplaza.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the UserService interface.
 * Handles business logic related to User entities.
 */
@Service // Marks this class as a Spring Service component.
public class UserServiceImpl implements UserService {

    @Autowired // Injects UserRepository for database interaction.
    private UserRepository userRepository;

    @Autowired // Injects ModelMapper for object mapping (Entity <-> DTO).
    private ModelMapper modelMapper;

    @Autowired // Injects PasswordEncoder for hashing passwords.
    private PasswordEncoder passwordEncoder;

    /**
     * Creates a new user in the system.
     * Validates if an email already exists, hashes the password, and sets default role.
     * @param userDto The UserDto containing user details to be created.
     * @return The created UserDto with generated ID and default values.
     * @throws ApiException if a user with the given email already exists.
     */
    @Override
    public UserDto createUser(UserDto userDto) {
        // Check if a user with the given email already exists.
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "User with this email already exists!");
        }

        // Map DTO to Entity.
        User user = modelMapper.map(userDto, User.class);
        // Encode the password before saving to the database for security.
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        // Set default role to USER if not specified, otherwise use the provided role.
        user.setRole(userDto.getRole() != null ? Role.valueOf(userDto.getRole().toUpperCase()) : Role.USER);
        user.setIsActive(true); // Ensure new users are active by default.

        // Save the new user to the database.
        User savedUser = userRepository.save(user);
        // Map the saved Entity back to DTO and return.
        return modelMapper.map(savedUser, UserDto.class);
    }

    /**
     * Retrieves a user by their unique ID.
     * @param userId The ID of the user to retrieve.
     * @return The UserDto of the found user.
     * @throws ResourceNotFoundException if no user is found with the given ID.
     */
    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return modelMapper.map(user, UserDto.class);
    }

    /**
     * Retrieves a user by their email address.
     * @param email The email address of the user to retrieve.
     * @return The UserDto of the found user.
     * @throws ResourceNotFoundException if no user is found with the given email.
     */
    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return modelMapper.map(user, UserDto.class);
    }

    /**
     * Retrieves a list of all registered users.
     * @return A list of UserDto objects.
     */
    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        // Stream through the list of User entities, map each to a UserDto, and collect into a new list.
        return users.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
    }

    /**
     * Updates an existing user's information.
     * Allows updating full name, phone, email (if not taken), password, and active status.
     * @param userId The ID of the user to update.
     * @param userDto The UserDto containing the updated user details.
     * @return The updated UserDto.
     * @throws ResourceNotFoundException if no user is found with the given ID.
     * @throws ApiException if the new email is already in use by another user.
     */
    @Override
    public UserDto updateUser(String userId, UserDto userDto) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        // Update fields if provided in DTO.
        Optional.ofNullable(userDto.getFullName()).ifPresent(existingUser::setFullName);
        Optional.ofNullable(userDto.getPhone()).ifPresent(existingUser::setPhone);
        Optional.ofNullable(userDto.getIsActive()).ifPresent(existingUser::setIsActive);

        // Check if email is being changed and if new email is already taken by another user.
        if (userDto.getEmail() != null && !userDto.getEmail().equals(existingUser.getEmail())) {
            if (userRepository.existsByEmail(userDto.getEmail())) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Email is already taken!");
            }
            existingUser.setEmail(userDto.getEmail());
        }

        // Update password if provided.
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        // Update role if provided, only if it's a valid role.
        if (userDto.getRole() != null) {
            try {
                existingUser.setRole(Role.valueOf(userDto.getRole().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid role specified: " + userDto.getRole());
            }
        }

        User updatedUser = userRepository.save(existingUser);
        return modelMapper.map(updatedUser, UserDto.class);
    }

    /**
     * Deletes a user from the system by their ID.
     * @param userId The ID of the user to delete.
     * @throws ResourceNotFoundException if no user is found with the given ID.
     */
    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        userRepository.delete(user);
    }

    /**
     * Checks if a user exists with the given email.
     * @param email The email to check.
     * @return True if a user exists, false otherwise.
     */
    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
