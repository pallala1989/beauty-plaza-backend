// config/SecurityConfig.java
package com.beautyplaza.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // Added for @PreAuthorize
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Enable @PreAuthorize and @PostAuthorize
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless APIs
                .authorizeHttpRequests(authorize -> authorize
                        // 1. PUBLIC ENDPOINTS - Permit ALL access to these first

                        // Authentication endpoints
                        .requestMatchers("/api/auth/**").permitAll()

                        // Swagger UI endpoints for API documentation
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()

                        // Actuator endpoints (for development, making info/env public for easier access)
                        // In production, secure /info and /env with .hasRole('ADMIN')
                        .requestMatchers("/actuator/health", "/actuator/info", "/actuator/env").permitAll()

                        // General public API endpoints
                        // These are the API paths that don't require any authentication
                        .requestMatchers(
                                "/api/services", // Get all active services
                                "/api/services/{id}", // Get specific service by ID
                                "/api/technicians", // Get all available technicians
                                "/api/technicians/{id}", // Get specific technician by ID
                                "/api/appointments/slots", // Get available slots
                                "/api/gift-cards/{cardCode}", // Get gift card details (e.g., balance check)
                                "/api/promotions", // Get all active promotions
                                "/api/promotions/{id}", // Get specific promotion by ID
                                "/api/referrals/{referralCode}/status", // Check referral status
                                "/api/contact" // Submit contact form
                        ).permitAll()

                        // 2. PROTECTED ENDPOINTS - Require ADMIN role
                        // Be very specific here. If a path starts with /api/admin/ or ends with /admin, secure it.
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/services/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/technicians/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/appointments/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/gift-cards/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/loyalty/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/promotions/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/referrals/admin/**").hasRole("ADMIN")
                        // And any other admin-specific endpoint you might add
                        .requestMatchers("/api/settings/admin/**").hasRole("ADMIN") // If SettingsController is under /api/settings/admin

                        // 3. AUTHENTICATED USER ENDPOINTS (requires any authenticated user)
                        // If you have endpoints that any logged-in user (not just ADMIN) can access
                        // Example: .requestMatchers("/api/profile/**").authenticated()

                        // IMPORTANT: The order matters!
                        // Any other request not explicitly permitted above, or matched by a specific role-based rule,
                        // will fall to this line. If you want only specific '/api/**' to be public,
                        // then remove the broad '/api/**'.permitAll() or place it carefully.
                        // For now, given your prior intention, the explicit permitAll() for common paths is best.
                        .anyRequest().authenticated() // All remaining requests require authentication
                )
                .httpBasic(withDefaults()) // Use HTTP Basic authentication for authenticated endpoints
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Stateless session
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Default 'user' for general authenticated access, and 'admin' for admin access.
        // Passwords are encrypted using BCrypt.
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("password")) // Default password 'password'
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("adminpass")) // Default password 'adminpass'
                .roles("ADMIN", "USER") // Admin also has USER role implicitly
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}