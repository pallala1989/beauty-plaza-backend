package com.beautyplaza.exception;

// Importing Spring Framework annotations and classes for exception handling.
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * GlobalExceptionHandler is a centralized exception handling mechanism for the REST API.
 * It uses @ControllerAdvice to provide global exception handling across all @Controller classes.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles specific ResourceNotFoundException.
     * When a ResourceNotFoundException is thrown, this method constructs a suitable error response.
     * @param exception The ResourceNotFoundException instance.
     * @param webRequest The current web request.
     * @return A ResponseEntity containing the error details and NOT_FOUND status.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                        WebRequest webRequest) {
        // Create ErrorDetails object with current timestamp, error message, and request description.
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND); // Return 404 Not Found.
    }

    /**
     * Handles specific ApiException.
     * When an ApiException is thrown, this method constructs a suitable error response.
     * @param exception The ApiException instance.
     * @param webRequest The current web request.
     * @return A ResponseEntity containing the error details and the custom status from ApiException.
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorDetails> handleApiException(ApiException exception,
                                                           WebRequest webRequest) {
        // Create ErrorDetails object with current timestamp, error message, and request description.
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, exception.getStatus()); // Return custom HTTP status.
    }

    /**
     * Handles validation errors (e.g., from @Valid annotation).
     * This method captures validation failures and returns a map of field errors.
     * @param ex The MethodArgumentNotValidException instance.
     * @return A ResponseEntity containing a map of field errors and BAD_REQUEST status.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        // Iterate over all field errors and add them to the map.
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); // Return 400 Bad Request.
    }

    /**
     * Handles all other unexpected exceptions.
     * This is a fallback handler for any exceptions not specifically caught by other handlers.
     * @param exception The general Exception instance.
     * @param webRequest The current web request.
     * @return A ResponseEntity containing generic error details and INTERNAL_SERVER_ERROR status.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception exception,
                                                              WebRequest webRequest) {
        // Create ErrorDetails object for generic errors.
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR); // Return 500 Internal Server Error.
    }
}
