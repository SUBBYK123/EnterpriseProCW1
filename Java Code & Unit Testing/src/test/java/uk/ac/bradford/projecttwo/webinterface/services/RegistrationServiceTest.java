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

public class RegistrationServiceTest {

    private RegistrationRepositoryImpl repository;
    private Encryptor encryptor;
    private EmailService emailService;
    private RegistrationService service;

    @BeforeEach
    void setUp() {
        repository = mock(RegistrationRepositoryImpl.class);
        encryptor = mock(Encryptor.class);
        emailService = mock(EmailService.class);

        service = new RegistrationService(repository);
        service.encryptor = encryptor;
        service.emailService = emailService;
    }

    @Test
    void testRegisterUser_SuccessfulRegistration() throws Exception {
        RegistrationModel newUser = new RegistrationModel();
        newUser.setEmailAddress("new@example.com");
        newUser.setFirstName("Test");
        newUser.setLastName("User");

        // Simulate: user does not exist
        when(repository.findUserByEmail("new@example.com")).thenReturn(null);
        when(repository.savePendingUser(newUser)).thenReturn(true);

        boolean result = service.registerUser(newUser);

        assertTrue(result);
        verify(repository).savePendingUser(newUser);
        verify(emailService).sendSignupNotificationToAdmin("Test User", "new@example.com");
    }

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

    @Test
    void testAuthenticateUser_SuccessfulMatch() {
        String email = "login@example.com";
        String rawPasswordConvertsIntoHashedPasswordFromSignupModel = "secret123";

        ;

        RegistrationModel mockUser = new RegistrationModel();
        mockUser.setEmailAddress(email);
        mockUser.setPassword(rawPasswordConvertsIntoHashedPasswordFromSignupModel);

        when(repository.findUserByEmail(email)).thenReturn(mockUser);

        boolean result = service.authenticateUser(email, rawPasswordConvertsIntoHashedPasswordFromSignupModel);
        assertTrue(result);
    }



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

    @Test
    void testAuthenticateUser_Failure_UserNotFound() {
        when(repository.findUserByEmail("nonexistent@example.com")).thenReturn(null);
        boolean result = service.authenticateUser("nonexistent@example.com", "any");
        assertFalse(result);
    }
}
