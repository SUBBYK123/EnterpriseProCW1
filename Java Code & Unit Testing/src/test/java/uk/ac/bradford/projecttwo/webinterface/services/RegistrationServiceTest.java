package uk.ac.bradford.projecttwo.webinterface.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCrypt;
import uk.ac.bradford.projecttwo.webinterface.models.RegistrationModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.RegistrationRepositoryImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit test for the RegistrationService class.
 * This test class verifies user registration and authentication functionality.
 */
@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {

    @Mock
    private RegistrationRepositoryImpl registrationRepository; // Mocked repository for dependency injection

    @InjectMocks
    private RegistrationService registrationService; // Service being tested

    private RegistrationModel user; // Test user instance

    /**
     * Sets up a mock user before each test.
     */
    @BeforeEach
    void setUp() {
        user = new RegistrationModel();
        user.setEmailAddress("test@example.com");
        user.setPassword("password123"); // Plaintext password for testing (will be hashed in real usage)
    }

    /**
     * Tests successful user registration.
     * The expected result is true.
     */
    @Test
    void testRegisterUser_Success() {
        // Simulate a new user who doesn't already exist
        when(registrationRepository.findUserByEmail(user.getEmailAddress())).thenReturn(null);
        when(registrationRepository.registerUser(any(RegistrationModel.class))).thenReturn(true);

        boolean result = registrationService.registerUser(user);

        assertTrue(result, "User registration should succeed for new users");
        verify(registrationRepository).registerUser(any(RegistrationModel.class));
    }

    /**
     * Tests registration when the user already exists.
     * The expected result is false.
     */
    @Test
    void testRegisterUser_UserAlreadyExists() {
        // Simulate an existing user
        when(registrationRepository.findUserByEmail(user.getEmailAddress())).thenReturn(user);

        boolean result = registrationService.registerUser(user);

        assertFalse(result, "Registration should fail if the email is already in use");
        verify(registrationRepository, never()).registerUser(any(RegistrationModel.class)); // Ensure registerUser() is not called
    }


    //Test keeps failing so needs fixing.
    /**
     * Tests successful user authentication using a fixed hashed password.
     * The expected result is true.
     */
    @Test
    void testAuthenticateUser_Success() {
        String rawPassword = "password123";
        String fixedHashedPassword = "$2a$10$Dow0rZH2hP0n5qyrOqTpUeFKr2eb3Z7p/64rwE/o1CnMy6vlrJb7G"; // Hash of "password123"

        user.setPassword(fixedHashedPassword);
        when(registrationRepository.findUserByEmail(user.getEmailAddress())).thenReturn(user);

        boolean result = registrationService.authenticateUser("test@example.com", rawPassword);

        assertTrue(result, "Authentication should succeed for the correct password");
    }

    /**
     * Tests authentication when the user does not exist.
     * The expected result is false.
     */
    @Test
    void testAuthenticateUser_UserNotFound() {
        when(registrationRepository.findUserByEmail("test@example.com")).thenReturn(null);

        boolean result = registrationService.authenticateUser("test@example.com", "password123");

        assertFalse(result, "Authentication should fail if the user is not found");
    }

    /**
     * Tests authentication when an incorrect password is provided.
     * The expected result is false.
     */
    @Test
    void testAuthenticateUser_IncorrectPassword() {
        String hashedPassword = BCrypt.hashpw("password123", BCrypt.gensalt());
        user.setPassword(hashedPassword);
        when(registrationRepository.findUserByEmail("test@example.com")).thenReturn(user);

        boolean result = registrationService.authenticateUser("test@example.com", "wrongpassword");

        assertFalse(result, "Authentication should fail for incorrect passwords");
    }
}
