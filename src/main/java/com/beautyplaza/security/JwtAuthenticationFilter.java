package com.beautyplaza.security;


// Importing necessary Java EE (Jakarta) and Spring Framework classes.
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Custom Spring Security filter to intercept incoming requests and validate JWT tokens.
 * This filter runs once per request to check for a valid JWT in the Authorization header.
 * If a valid token is found, it extracts user details and sets them in the SecurityContext.
 */
@Component // Marks this class as a Spring component.
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired // Injects JwtHelper for JWT token generation and validation.
    private JwtHelper jwtHelper;

    @Autowired // Injects CustomUserDetailsService to load user details.
    private CustomUserDetailsService userDetailsService;

    /**
     * Performs filtering for every request.
     * It extracts the JWT from the request header, validates it, and sets the authentication
     * in the SecurityContext if the token is valid.
     *
     * @param request The HttpServletRequest.
     * @param response The HttpServletResponse.
     * @param filterChain The FilterChain to continue processing the request.
     * @throws ServletException If a servlet-specific error occurs.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Get Authorization header from the request.
        String requestHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;

        // Check if the header exists and starts with "Bearer ".
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {
            // Extract the token (remove "Bearer " prefix).
            token = requestHeader.substring(7);
            try {
                // Extract username (email) from the token.
                username = this.jwtHelper.getUsernameFromToken(token);
            } catch (IllegalArgumentException e) {
                logger.error("Illegal Argument while fetching the username !! " + e.getMessage());
            } catch (io.jsonwebtoken.ExpiredJwtException e) {
                logger.error("Given jwt token is expired !! " + e.getMessage());
            } catch (io.jsonwebtoken.MalformedJwtException e) {
                logger.error("Some changed in token !! Invalid Token" + e.getMessage());
            } catch (Exception e) {
                logger.error("An unexpected error occurred while parsing JWT: " + e.getMessage());
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String or is null");
        }

        // If username is extracted and no authentication is currently set in the SecurityContext.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load UserDetails by username (email).
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            // Validate the token against the loaded user details.
            if (this.jwtHelper.validateToken(token, userDetails)) {
                // Create an authentication token.
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                // Set authentication details from the request.
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Set the authentication in the SecurityContext.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                logger.error("Validation fails !!");
            }
        }

        // Continue the filter chain.
        filterChain.doFilter(request, response);
    }
}
