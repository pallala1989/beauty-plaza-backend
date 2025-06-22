// config/SecurityConfig.java
package com.beautyplaza.config;

// Importing necessary Spring Security and other Spring Framework classes.
import com.beautyplaza.security.JwtAuthenticationEntryPoint;
import com.beautyplaza.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


// @Configuration indicates that this class contains Spring configuration beans.
// @EnableWebSecurity enables Spring Security's web security support and provides the Spring Security integration.
// @EnableMethodSecurity enables method-level security (e.g., @PreAuthorize).
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // Autowire JwtAuthenticationEntryPoint to handle unauthorized access attempts.
    @Autowired
    private JwtAuthenticationEntryPoint point;

    // Autowire JwtAuthenticationFilter to intercept requests and validate JWT tokens.
    @Autowired
    private JwtAuthenticationFilter filter;

    /**
     * Configures the security filter chain for HTTP requests.
     * This method defines authorization rules for various endpoints, sets up JWT authentication,
     * and configures session management.
     *
     * @param http The HttpSecurity object to configure.
     * @return A configured SecurityFilterChain.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection as JWT handles stateless authentication.
                // Referencing the corsConfigurationSource bean directly
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        // Allow unauthenticated access to authentication and public endpoints.
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll() // Swagger UI paths
                        // All other requests require authentication.
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(point)) // Configure custom authentication entry point.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Use stateless sessions.

        // Add the JWT authentication filter before the UsernamePasswordAuthenticationFilter.
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return http.build(); // Build and return the security filter chain.
    }

    /**
     * Configures the PasswordEncoder bean.
     * Uses BCryptPasswordEncoder for strong password hashing.
     * @return A PasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provides the AuthenticationManager bean.
     * The AuthenticationManager is responsible for authenticating users.
     * @param builder The AuthenticationConfiguration object.
     * @return An AuthenticationManager instance.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }

    /**
     * Configures and provides the UrlBasedCorsConfigurationSource bean.
     * This defines the CORS policy for the application, specifying allowed origins, methods, and headers.
     * This bean is used by the Spring Security's CORS integration.
     * @return A UrlBasedCorsConfigurationSource instance.
     */
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // Allow credentials (e.g., cookies, authorization headers).
        config.addAllowedOrigin("http://localhost:3000"); // Allow specific origin for frontend.
        config.addAllowedOrigin("http://localhost:8080"); // Allow backend origin for internal testing.
        config.addAllowedOriginPattern("*"); // Allow all origins during development.
        config.addAllowedHeader("*"); // Allow all headers.
        config.addAllowedMethod("*"); // Allow all HTTP methods (GET, POST, PUT, DELETE, etc.).
        source.registerCorsConfiguration("/**", config); // Apply this CORS configuration to all paths.
        return source;
    }

    /**
     * Provides the CorsFilter bean. This is generally not needed if CorsConfigurationSource is handled
     * by Spring Security's .cors() method, but can be useful for explicit filter chain management.
     * Keeping it here for completeness, though it might be redundant with the cors() customizer above.
     * @return A CorsFilter instance.
     */
    @Bean
    public CorsFilter corsFilter() {
        // This CorsFilter explicitly uses the corsConfigurationSource bean defined above.
        return new CorsFilter(corsConfigurationSource());
    }
}
