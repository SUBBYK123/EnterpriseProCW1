package uk.ac.bradford.projecttwo.webinterface.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for the Encryptor class to verify password encryption using BCrypt.
 */
public class EncryptorTest {

    // Instance of Encryptor for testing
    private final Encryptor encryptor = new Encryptor();

    /**
     * Tests the encryptString() method to ensure that the raw password
     * is properly hashed and can be verified using BCrypt's matches() method.
     */
    @Test
    public void testEncryptString() {
        // Raw password to be hashed
        String rawPassword = "password";

        // Encrypt the raw password
        String encryptedPassword = encryptor.encryptString(rawPassword);

        // BCrypt encoder for verification
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Debugging output (optional, but avoid logging passwords in production)
        System.out.println("Raw Password: " + rawPassword);
        System.out.println("Encrypted Password: " + encryptedPassword);

        // Verify that the raw password matches the hashed password
        assertTrue(encoder.matches(rawPassword, encryptedPassword),
                "The encrypted password should match the raw password when verified using BCrypt.");
    }
}
