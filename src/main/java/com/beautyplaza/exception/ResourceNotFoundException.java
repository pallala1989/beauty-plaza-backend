// exception/ResourceNotFoundException.java
package com.beautyplaza.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception to indicate that a requested resource was not found.
 * This exception will result in an HTTP 404 Not Found response.
 */
@ResponseStatus(HttpStatus.NOT_FOUND) // Sets the HTTP status code for this exception.
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L; // Serial version UID for serialization.
    private final String resourceName; // Name of the resource that was not found (e.g., "User", "Service").
    private final String fieldName;    // Name of the field used for lookup (e.g., "id", "email").
    private final Object fieldValue;   // Value of the field that failed to find the resource.

    /**
     * Constructs a new ResourceNotFoundException with details about the missing resource.
     * @param resourceName The name of the resource (e.g., "User").
     * @param fieldName The name of the field used for the lookup (e.g., "id").
     * @param fieldValue The value of the field that was searched (e.g., 1, "test@example.com").
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        // Constructs a detailed error message using String.format.
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    // Getter methods for exception details.
    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}
