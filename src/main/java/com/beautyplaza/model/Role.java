package com.beautyplaza.model;

/**
 * Enum representing the roles a user can have in the Beauty Plaza application.
 * This helps in implementing role-based access control.
 */
public enum Role {
    ADMIN,      // Administrator role with full access.
    TECHNICIAN, // Technician role, can manage appointments and services they perform.
    USER        // Regular user role, can book appointments and manage their profile.
}