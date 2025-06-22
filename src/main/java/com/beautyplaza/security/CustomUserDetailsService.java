package com.beautyplaza.security;


// Importing necessary Spring Security and other classes.
import com.beautyplaza.model.User;
import com.beautyplaza.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

/**
 * Custom implementation of Spring Security's UserDetailsService.
 * This service is responsible for loading user-specific data during the authentication process.
 */
@Service // Marks this class as a Spring Service component.
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired // Injects UserRepository to fetch user details from the database.
    private UserRepository userRepository;

    /**
     * Loads user-specific data by their username (in this case, email).
     * This method is called by Spring Security to retrieve user details.
     * @param username The username (email) of the user to load.
     * @return UserDetails object containing user information and authorities.
     * @throws UsernameNotFoundException if no user is found with the given username.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch user from the database by email.
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        // Create a GrantedAuthority list from the user's role.
        // Spring Security expects roles to be prefixed with "ROLE_".
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        // Return a Spring Security User object.
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),          // Username (email)
                user.getPassword(),       // Hashed password
                user.getIsActive(),       // Account enabled status
                true,                     // Account non-expired
                true,                     // Credentials non-expired
                true,                     // Account non-locked
                authorities               // User's authorities (roles)
        );
    }
}
