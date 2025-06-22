package com.beautyplaza.security;

// Importing necessary JWT and Spring Security classes.
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Helper class for JWT (JSON Web Token) operations.
 * Provides methods for generating, validating, and extracting information from JWT tokens.
 */
@Component // Marks this class as a Spring component.
public class JwtHelper {

    // Secret key for JWT signing, loaded from application properties.
    // It should be a strong, randomly generated secret.
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    // Token validity duration in milliseconds, loaded from application properties.
    @Value("${jwt.expiration}")
    private long JWT_TOKEN_VALIDITY; // 5 hours in milliseconds

    /**
     * Retrieves the username (subject) from the JWT token.
     * @param token The JWT token.
     * @return The username (email) extracted from the token.
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Retrieves the expiration date from the JWT token.
     * @param token The JWT token.
     * @return The expiration Date of the token.
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * Retrieves a specific claim from the JWT token using a claims resolver function.
     * @param token The JWT token.
     * @param claimsResolver A function to resolve a specific claim from the Claims object.
     * @param <T> The type of the claim.
     * @return The resolved claim.
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Parses the JWT token and extracts all claims.
     * @param token The JWT token.
     * @return The Claims object containing all claims from the token.
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // Set the signing key for parsing.
                .build()
                .parseClaimsJws(token)
                .getBody(); // Get the body (claims) of the JWT.
    }

    /**
     * Checks if the token has expired.
     * @param token The JWT token.
     * @return True if the token is expired, false otherwise.
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date()); // Check if expiration date is before current date.
    }

    /**
     * Generates a JWT token for the given user details.
     * @param userDetails The UserDetails object containing user information.
     * @return The generated JWT token string.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // Add user role as a claim.
        claims.put("role", userDetails.getAuthorities().iterator().next().getAuthority());
        return doGenerateToken(claims, userDetails.getUsername());
    }

    /**
     * Does the actual generation of the token.
     * Defines claims of the token, subject, issued and expiration dates, and signs the token.
     * @param claims Custom claims to be included in the token.
     * @param subject The subject of the token (username/email).
     * @return The JWT token string.
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims) // Set custom claims.
                .setSubject(subject) // Set the subject (username).
                .setIssuedAt(new Date(System.currentTimeMillis())) // Set issued date to current time.
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000)) // Set expiration date.
                .signWith(getSigningKey(), SignatureAlgorithm.HS512) // Sign the token with HS512 algorithm and secret key.
                .compact(); // Build and compact the JWT into a string.
    }

    /**
     * Validates the JWT token.
     * Checks if the username matches and if the token is not expired.
     * @param token The JWT token to validate.
     * @param userDetails The UserDetails object to validate against.
     * @return True if the token is valid, false otherwise.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Retrieves the signing key from the secret string.
     * The secret key is base64 decoded to create a secure key.
     * @return The Key object used for signing/verifying JWTs.
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
