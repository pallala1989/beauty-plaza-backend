package com.beautyplaza.exception;

import org.springframework.http.HttpStatus;

/**
 * Custom exception for general API-related errors.
 * This can be used for various business logic errors that don't fit into
 * specific HTTP status codes like 404 (Not Found).
 */
public class ApiException extends RuntimeException {

    private static final long serialVersionUID = 1L; // Serial version UID for serialization.
    private final HttpStatus status; // The HTTP status to return with this exception.
    private final String message;    // A custom message describing the API error.

    /**
     * Constructs a new ApiException.
     * @param status The HTTP status code (e.g., HttpStatus.BAD_REQUEST, HttpStatus.FORBIDDEN).
     * @param message A descriptive message for the error.
     */
    public ApiException(HttpStatus status, String message) {
        super(message); // Call the superclass constructor with the message.
        this.status = status;
        this.message = message;
    }

    /**
     * Constructs a new ApiException with a cause.
     * @param status The HTTP status code.
     * @param message A descriptive message for the error.
     * @param cause The underlying cause of the exception.
     */
    public ApiException(HttpStatus status, String message, Throwable cause) {
        super(message, cause); // Call the superclass constructor with message and cause.
        this.status = status;
        this.message = message;
    }

    // Getter methods for exception details.
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
