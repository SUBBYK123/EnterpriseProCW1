package uk.ac.bradford.projecttwo.webinterface.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * The Encryptor class provides encryption functionality for sensitive data, such as passwords.
 * It uses BCryptPasswordEncoder from Spring Security to securely hash passwords.
 */
@Component
public class Encryptor {

    // PasswordEncoder instance using BCrypt for hashing passwords securely
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Encrypts a given input string (typically a password) using BCrypt hashing.
     *
     * @param input The string to be encrypted.
     * @return The encrypted (hashed) version of the input string.
     */
    public String encryptString(String input) {
        return passwordEncoder.encode(input);
    }
}
