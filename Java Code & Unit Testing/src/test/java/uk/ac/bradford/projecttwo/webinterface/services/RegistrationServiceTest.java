package uk.ac.bradford.projecttwo.webinterface.services;

import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import uk.ac.bradford.projecttwo.webinterface.models.RegistrationModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.RegistrationRepositoryImpl;
import uk.ac.bradford.projecttwo.webinterface.security.Encryptor;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link RegistrationService}, which handles logic
 * related to user registration, email uniqueness checking, and authentication.
 */
public class RegistrationServiceTest {

    private RegistrationRepositoryImpl repository;
    private Encryptor encryptor;
    private EmailService emailService;
    private RegistrationService service;

    /**
     * Initializes the service with mocked dependencies before each test.
     */
    @BeforeEach
    void setUp() {
        repository = mock(RegistrationRepositoryImpl.class);
        encryptor = mock(Encryptor.class);
        emailService = mock(EmailService.class);

        service = new RegistrationService(repository);
        service.encryptor = encryptor;
        service.emailService = emailService;
    }

    /**
     * Tests successful user registration:
     * - Email does not already exist
     * - User is saved as pending
     * - Admin notification is sent
     */
    @Test
    void testRegisterUser_SuccessfulRegistration() throws Exception {
        RegistrationModel newUser = new RegistrationModel();
        newUser.setEmailAddress("new@example.com");
        newUser.setFirstName("Test");
        newUser.setLastName("User");

        when(repository.findUserByEmail("new@example.com")).thenReturn(null);
        when(repository.savePendingUser(newUser)).thenReturn(true);

        boolean result = service.registerUser(newUser);

        assertTrue(result);
        verify(repository).savePendingUser(newUser);
        verify(emailService).sendSignupNotificationToAdmin("Test User", "new@example.com");
    }

    /**
     * Tests that registration is blocked when the email is already taken.
     * Ensures no save or email operations occur.
     */
    @Test
    void testRegisterUser_EmailAlreadyExists() throws IOException, MessagingException {
        RegistrationModel existingUser = new RegistrationModel();
        existingUser.setEmailAddress("existing@example.com");

        when(repository.findUserByEmail("existing@example.com")).thenReturn(existingUser);

        boolean result = service.registerUser(existingUser);

        assertFalse(result);
        verify(repository, never()).savePendingUser(any());
        verify(emailService, never()).sendSignupNotificationToAdmin(any(), any());
    }

    /**
     * Tests that a user is successfully authenticated when the email and password match.
     */
    @Test
    void testAuthenticateUser_SuccessfulMatch() {
        String email = "login@example.com";
        String password = "secret123";

        RegistrationModel mockUser = new RegistrationModel();
        mockUser.setEmailAddress(email);
        mockUser.setPassword(password); // Simulated plain text password (for test scenario)

        when(repository.findUserByEmail(email)).thenReturn(mockUser);

        boolean result = service.authenticateUser(email, password);
        assertTrue(result);
    }

    /**
     * Tests that authentication fails when the password does not match the stored hash.
     */
    @Test
    void testAuthenticateUser_Failure_WrongPassword() {
        String email = "login@example.com";

        RegistrationModel user = new RegistrationModel();
        user.setEmailAddress(email);
        user.setPassword(org.springframework.security.crypto.bcrypt.BCrypt.hashpw("correctpassword", org.springframework.security.crypto.bcrypt.BCrypt.gensalt()));

        when(repository.findUserByEmail(email)).thenReturn(user);

        boolean result = service.authenticateUser(email, "wrongpassword");
        assertFalse(result);
    }

    /**
     * Tests that authentication fails when no user is found by email.
     */
    @Test
    void testAuthenticateUser_Failure_UserNotFound() {
        when(repository.findUserByEmail("nonexistent@example.com")).thenReturn(null);
        boolean result = service.authenticateUser("nonexistent@example.com", "any");
        assertFalse(result);
    }
}
