package uk.ac.bradford.projecttwo.webinterface.security;

import java.security.SecureRandom;

/**
 * Utility class for generating One-Time Passwords (OTPs).
 * Uses a cryptographically secure random number generator to create 6-digit numeric codes.
 */
public class OTPUtil {

    // SecureRandom provides strong randomness suitable for security-sensitive applications
    private static final SecureRandom random = new SecureRandom();

    /**
     * Generates a 6-digit numeric One-Time Password (OTP).
     *
     * @return A string representing a 6-digit OTP (e.g., "029374").
     */
    public static String generateOTP() {
        return String.format("%06d", random.nextInt(1000000));
    }
}
