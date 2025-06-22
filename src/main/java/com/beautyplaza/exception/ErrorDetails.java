package com.beautyplaza.exception;

// Importing Java utilities for Date.
import java.util.Date;

/**
 * A simple POJO (Plain Old Java Object) to encapsulate error details
 * that will be returned in the API response when an exception occurs.
 */
public class ErrorDetails {
    private final Date timestamp; // The time when the error occurred.
    private final String message; // A descriptive message about the error.
    private final String details; // Further details about the error, often including the request URI.

    /**
     * Constructs a new ErrorDetails object.
     * @param timestamp The timestamp of the error.
     * @param message A brief message about the error.
     * @param details More specific details, often including the request path.
     */
    public ErrorDetails(Date timestamp, String message, String details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    // Getter methods for all fields to allow serialization to JSON.
    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}
