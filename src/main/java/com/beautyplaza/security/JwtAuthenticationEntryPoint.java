package com.beautyplaza.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Custom AuthenticationEntryPoint for handling unauthorized access attempts in a JWT-based authentication system.
 * When an unauthenticated user tries to access a protected resource, this class is invoked.
 * It sends an HTTP 401 Unauthorized response to the client.
 */
@Component // Marks this class as a Spring component, allowing it to be auto-detected and registered.
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Commences an authentication scheme.
     * This method is called when an unauthenticated user attempts to access a protected resource.
     * @param request The HttpServletRequest.
     * @param response The HttpServletResponse.
     * @param authException The AuthenticationException that occurred.
     * @throws IOException If an I/O error occurs during the response writing.
     * @throws ServletException If a servlet-specific error occurs.
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        // Set the HTTP status to 401 Unauthorized.
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // Get a PrintWriter to write the response body.
        PrintWriter writer = response.getWriter();
        // Write a message indicating unauthorized access.
        writer.println("Access Denied !! " + authException.getMessage());
    }
}
