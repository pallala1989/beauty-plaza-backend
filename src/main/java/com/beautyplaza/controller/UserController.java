package com.beautyplaza.controller;

// Importing necessary Spring Framework, DTO, and security classes.
import com.beautyplaza.dto.UserDto;
import com.beautyplaza.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST Controller for managing User entities.
 * Provides endpoints for CRUD operations on users, with role-based access control.
 */
@RestController // Marks this class as a REST controller.
@RequestMapping("/api/users") // Base path for all endpoints in this controller.
public class UserController {

    @Autowired // Injects UserService for business logic operations on users.
    private UserService userService;

    /**
     * Creates a new user. Accessible by ADMIN only.
     * @param userDto The UserDto containing user details.
     * @return ResponseEntity with the created UserDto.
     */
    @PreAuthorize("hasRole('ADMIN')") // Only users with ADMIN role can access this.
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto createdUser = userService.createUser(userDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED); // Return 201 Created.
    }

    /**
     * Retrieves a user by ID. Accessible by ADMIN or the user themselves.
     * Uses SpEL (Spring Expression Language) for more granular access control.
     * `authentication.principal.username` refers to the email of the logged-in user.
     * @param id The ID of the user to retrieve.
     * @return ResponseEntity with the UserDto.
     */
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.username")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user); // Return 200 OK.
    }

    /**
     * Retrieves a user by email. Accessible by ADMIN.
     * @param email The email of the user to retrieve.
     * @return ResponseEntity with the UserDto.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        UserDto user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user); // Return 200 OK.
    }

    /**
     * Retrieves all users. Accessible by ADMIN only.
     * @return ResponseEntity with a list of UserDtos.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users); // Return 200 OK.
    }

    /**
     * Updates an existing user. Accessible by ADMIN or the user themselves.
     * @param id The ID of the user to update.
     * @param userDto The UserDto containing updated details.
     * @return ResponseEntity with the updated UserDto.
     */
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.username")
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable String id, @Valid @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(id, userDto);
        return ResponseEntity.ok(updatedUser); // Return 200 OK.
    }

    /**
     * Deletes a user by ID. Accessible by ADMIN only.
     * @param id The ID of the user to delete.
     * @return ResponseEntity with no content.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content.
    }
}
