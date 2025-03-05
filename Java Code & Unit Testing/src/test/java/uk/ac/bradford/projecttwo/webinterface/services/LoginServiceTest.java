package uk.ac.bradford.projecttwo.webinterface.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCrypt;
import uk.ac.bradford.projecttwo.webinterface.models.LoginModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.UserRepositoryImpl;
import uk.ac.bradford.projecttwo.webinterface.security.Encryptor;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @Mock
    private UserRepositoryImpl loginRepository;

    @InjectMocks
    private LoginServiceImpl loginService;

    private LoginModel mockUser;

    @BeforeEach
    void setUpMockUser(){
        Encryptor encryptor = null;
        String hashedPassword = BCrypt.hashpw("password123",BCrypt.gensalt());
        String testEmail = "test@example.com";
        mockUser = new LoginModel(testEmail,hashedPassword);
    }

    @Test
    void authenticateUser_CorrectPassword_ReturnsTrue(){
        when(loginRepository.findUserByEmail("test@example.com")).thenReturn(mockUser);

        boolean result = loginService.authenticateUser("test@example.com","password123");

        assertTrue(result,"Authentication should succeed for correct credentials");
    }

    @Test
    void authenticateUser_WrongPassword_ReturnsFalse(){
        when(loginRepository.findUserByEmail("test@example.com")).thenReturn(mockUser);

        boolean result = loginService.authenticateUser("test@example.com","wrongpassword");

        assertFalse(result,"Authentication should fail for incorrect password");
    }

    @Test
    void authenticateUser_NonExistentEmail_ReturnsFalse() {
        when(loginRepository.findUserByEmail("unknown@example.com")).thenReturn(null);

        boolean result = loginService.authenticateUser("unknown@example.com", "password123");

        assertFalse(result, "Authentication should fail for non-existent email");
    }

    @Test
    void getUserByEmail_ExistingUser_ReturnsUser(){
        when(loginRepository.findUserByEmail("test@example.com")).thenReturn(mockUser);

        LoginModel result = loginRepository.findUserByEmail("test@example.com");

        assertNotNull(result,"User should be found");
        assertEquals("test@example.com",result.getEmailAddress(),"emails should match");
    }

    @Test
    void getUserByEmail_NonExistentUser_ReturnsNull() {
        when(loginRepository.findUserByEmail("unknown@example.com")).thenReturn(null);

        LoginModel result = loginService.findUserByEmail("unknown@example.com");

        assertNull(result, "User should not be found");
    }

    // Test case 6: Fetch all users
    @Test
    void getAllUsers_ReturnsUserList() {
        List<LoginModel> mockUsers = Arrays.asList(
                new LoginModel("user1@example.com", "pass1"),
                new LoginModel("user2@example.com", "pass2")
        );
    }

}
