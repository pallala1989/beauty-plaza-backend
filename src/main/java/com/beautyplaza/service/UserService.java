// service/AuthService.java (NEW)
package com.beautyplaza.service;

// Importing the UserDto and Java utilities.
import com.beautyplaza.dto.UserDto;
import java.util.List;

/**
 * Interface for User-related business logic.
 * Defines the contract for operations on User data.
 */
public interface UserService {
    /**
     * Creates a new user.
     * @param userDto The UserDto containing user details.
     * @return The created UserDto.
     */
    UserDto createUser(UserDto userDto);

    /**
     * Retrieves a user by their ID.
     * @param userId The ID of the user to retrieve.
     * @return The UserDto of the found user.
     */
    UserDto getUserById(String userId);

    /**
     * Retrieves a user by their email.
     * @param email The email of the user to retrieve.
     * @return The UserDto of the found user.
     */
    UserDto getUserByEmail(String email);

    /**
     * Retrieves all users.
     * @return A list of all UserDtos.
     */
    List<UserDto> getAllUsers();

    /**
     * Updates an existing user.
     * @param userId The ID of the user to update.
     * @param userDto The UserDto containing updated details.
     * @return The updated UserDto.
     */
    UserDto updateUser(String userId, UserDto userDto);

    /**
     * Deletes a user by their ID.
     * @param userId The ID of the user to delete.
     */
    void deleteUser(String userId);

    /**
     * Checks if a user exists by email.
     * @param email The email to check.
     * @return True if the user exists, false otherwise.
     */
    Boolean existsByEmail(String email);
}
