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

class LoginServiceImplTest {

    private UserRepositoryImpl mockRepository;
    private LoginServiceImpl loginService;

    @BeforeEach
    void setUp() {
        mockRepository = mock(UserRepositoryImpl.class);
        loginService = new LoginServiceImpl(mockRepository);
    }

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

    @Test
    void testFindUserByEmail_UserExists() {
        String email = "findme@example.com";
        LoginModel user = new LoginModel(email, "hashedPass", "ROLE_USER");

        when(mockRepository.findUserByEmail(email)).thenReturn(user);

        LoginModel found = loginService.findUserByEmail(email);
        assertNotNull(found);
        assertEquals(email, found.getEmailAddress());
    }

    @Test
    void testFindUserByEmail_UserNotFound() {
        when(mockRepository.findUserByEmail("notfound@example.com")).thenReturn(null);
        assertNull(loginService.findUserByEmail("notfound@example.com"));
    }

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
