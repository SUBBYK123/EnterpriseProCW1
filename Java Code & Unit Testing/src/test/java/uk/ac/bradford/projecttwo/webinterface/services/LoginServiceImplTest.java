package uk.ac.bradford.projecttwo.webinterface.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import uk.ac.bradford.projecttwo.webinterface.models.LoginModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.UserRepositoryImpl;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link LoginServiceImpl}, which manages user authentication and user account operations.
 */
class LoginServiceImplTest {

    private UserRepositoryImpl mockRepository;
    private LoginServiceImpl loginService;

    /**
     * Sets up a mocked user repository and injects it into the login service before each test.
     */
    @BeforeEach
    void setUp() {
        mockRepository = mock(UserRepositoryImpl.class);
        loginService = new LoginServiceImpl(mockRepository);
    }

    /**
     * Tests successful user authentication when correct credentials are provided.
     */
    @Test
    void testAuthenticateUser_ValidCredentials() {
        String email = "test@example.com";
        String rawPassword = "password123";
        String hashedPassword = org.springframework.security.crypto.bcrypt.BCrypt.hashpw(rawPassword, org.springframework.security.crypto.bcrypt.BCrypt.gensalt());

        LoginModel mockUser = new LoginModel(email, hashedPassword, "ROLE_USER");

        when(mockRepository.findUserByEmail(email)).thenReturn(mockUser);

        assertTrue(loginService.authenticateUser(email, rawPassword));
        verify(mockRepository).findUserByEmail(email);
    }

    /**
     * Tests authentication failure when an incorrect password is used.
     */
    @Test
    void testAuthenticateUser_InvalidPassword() {
        String email = "test@example.com";
        String correctPassword = "correctPass";
        String wrongPassword = "wrongPass";
        String hashed = org.springframework.security.crypto.bcrypt.BCrypt.hashpw(correctPassword, org.springframework.security.crypto.bcrypt.BCrypt.gensalt());

        LoginModel mockUser = new LoginModel(email, hashed, "ROLE_USER");

        when(mockRepository.findUserByEmail(email)).thenReturn(mockUser);

        assertFalse(loginService.authenticateUser(email, wrongPassword));
    }

    /**
     * Verifies that a user can be retrieved correctly by email if they exist.
     */
    @Test
    void testFindUserByEmail_UserExists() {
        String email = "findme@example.com";
        LoginModel user = new LoginModel(email, "hashedPass", "ROLE_USER");

        when(mockRepository.findUserByEmail(email)).thenReturn(user);

        LoginModel found = loginService.findUserByEmail(email);
        assertNotNull(found);
        assertEquals(email, found.getEmailAddress());
    }

    /**
     * Ensures null is returned when a user is not found by email.
     */
    @Test
    void testFindUserByEmail_UserNotFound() {
        when(mockRepository.findUserByEmail("notfound@example.com")).thenReturn(null);
        assertNull(loginService.findUserByEmail("notfound@example.com"));
    }

    /**
     * Tests that all users are retrieved from the repository as a list.
     */
    @Test
    void testGetAllUsers_ReturnsList() {
        List<LoginModel> users = Arrays.asList(
                new LoginModel("user1@example.com", "pass1", "ROLE_USER"),
                new LoginModel("user2@example.com", "pass2", "ROLE_ADMIN")
        );

        when(mockRepository.getAllUsers()).thenReturn(users);

        List<LoginModel> result = loginService.getAllUsers();
        assertEquals(2, result.size());
        verify(mockRepository).getAllUsers();
    }

    /**
     * Tests that user passwords are hashed before being updated in the repository.
     * Also checks that the hash matches the raw password.
     */
    @Test
    void testUpdateUserPassword_HashesCorrectly() {
        String email = "change@example.com";
        String newPassword = "newSecret123";

        loginService.updateUserPassword(email, newPassword);

        verify(mockRepository).updateUserPassword(eq(email), argThat(hashed -> {
            // ✅ Must match BCrypt format and check if matches new password
            return org.springframework.security.crypto.bcrypt.BCrypt.checkpw(newPassword, hashed);
        }));
    }
}
