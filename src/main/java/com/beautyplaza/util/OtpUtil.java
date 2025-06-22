package com.beautyplaza.util;

// Importing Java utilities.
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Utility class for generating and validating One-Time Passwords (OTPs).
 * This class provides a basic in-memory OTP storage for demonstration purposes.
 * In a production environment, OTPs would be stored more securely (e.g., Redis)
 * and sent via external services (SMS, email).
 */
@Component // Marks this class as a Spring component.
public class OtpUtil {

    // In-memory map to store OTPs. Key: email, Value: OTP.
    private final Map<String, String> otpStore = new HashMap<>();
    // In-memory map to store OTP generation timestamps. Key: email, Value: timestamp.
    private final Map<String, Long> otpTimestamps = new HashMap<>();
    // OTP validity duration in milliseconds (e.g., 5 minutes).
    private static final long OTP_VALIDITY_DURATION_MS = 5 * 60 * 1000; // 5 minutes

    /**
     * Generates a 6-digit OTP and stores it with a timestamp.
     * @param email The email address for which the OTP is generated.
     * @return The generated OTP string.
     */
    public String generateOtp(String email) {
        // Generate a 6-digit random number.
        String otp = String.format("%06d", new Random().nextInt(1000000));
        otpStore.put(email, otp); // Store the OTP.
        otpTimestamps.put(email, System.currentTimeMillis()); // Store the generation timestamp.
        return otp;
    }

    /**
     * Validates an OTP for a given email.
     * Checks if the OTP exists, matches, and is still valid (not expired).
     * @param email The email for which the OTP is to be validated.
     * @param otp The OTP provided by the user.
     * @return True if the OTP is valid, false otherwise.
     */
    public boolean validateOtp(String email, String otp) {
        // Check if OTP exists for the email.
        if (!otpStore.containsKey(email)) {
            return false;
        }

        // Check if OTP has expired.
        long generationTime = otpTimestamps.getOrDefault(email, 0L);
        if (System.currentTimeMillis() - generationTime > OTP_VALIDITY_DURATION_MS) {
            // OTP expired, remove it.
            otpStore.remove(email);
            otpTimestamps.remove(email);
            return false;
        }

        // Validate if the provided OTP matches the stored one.
        boolean isValid = otpStore.get(email).equals(otp);
        if (isValid) {
            // Remove OTP after successful validation to prevent reuse.
            otpStore.remove(email);
            otpTimestamps.remove(email);
        }
        return isValid;
    }
}
