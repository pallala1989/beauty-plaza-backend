// repository/AuthUserRepository.java (NEW)
package com.beautyplaza.repository;

// Importing the User entity and Spring Data JPA's JpaRepository.
import com.beautyplaza.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository interface for User entities.
 * Extends JpaRepository to provide standard CRUD operations for User objects.
 * The generic parameters are: User (the entity type) and String (the ID type of the entity).
 */
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * Finds a User by their email address.
     * Spring Data JPA automatically generates the query based on the method name.
     * @param email The email address to search for.
     * @return An Optional containing the found User, or empty if no user with that email exists.
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user exists with the given email address.
     * @param email The email address to check.
     * @return True if a user with the email exists, false otherwise.
     */
    Boolean existsByEmail(String email);
}