// model/AuthUser.java (NEW - Renamed from 'User' to avoid potential conflicts and provide context)
package com.beautyplaza.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// This would typically be a more complex User entity with roles, enabled status etc.
// For now, it's a basic placeholder.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users") // Database table for users
public class AuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password; // Store hashed password!
    @Column(unique = true)
    private String email;
    // You might add roles, enabled status, etc.
}