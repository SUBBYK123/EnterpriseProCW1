//package uk.ac.bradford.projecttwo.webinterface.services;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.crypto.bcrypt.BCrypt;
//import uk.ac.bradford.projecttwo.webinterface.models.LoginModel;
//import uk.ac.bradford.projecttwo.webinterface.repositories.UserRepositoryImpl;
//import uk.ac.bradford.projecttwo.webinterface.security.Encryptor;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
///**
// * Unit test for the LoginServiceImpl class.
// * This test class verifies authentication and user retrieval functionality.
// */
//@ExtendWith(MockitoExtension.class)
//public class LoginServiceTest {
//
//    @Mock
//    private UserRepositoryImpl loginRepository; // Mocked repository for dependency injection
//
//    @InjectMocks
//    private LoginServiceImpl loginService; // Service being tested
//
//    private LoginModel mockUser; // Test user instance
//
//    /**
//     * Sets up a mock user before each test.
//     */
//    @BeforeEach
//    void setUpMockUser() {
//        String hashedPassword = BCrypt.hashpw("password123", BCrypt.gensalt()); // Hash a test password
//        String testEmail = "test@example.com";
//        mockUser = new LoginModel(testEmail, hashedPassword); // Create mock user with hashed password
//    }
//
//    /**
//     * Tests authentication with the correct password.
//     * The expected result is true.
//     */
//    @Test
//    void authenticateUser_CorrectPassword_ReturnsTrue() {
//        when(loginRepository.findUserByEmail("test@example.com")).thenReturn(mockUser);
//
//        boolean result = loginService.authenticateUser("test@example.com", "password123");
//
//        assertTrue(result, "Authentication should succeed for correct credentials");
//    }
//
//    /**
//     * Tests authentication with an incorrect password.
//     * The expected result is false.
//     */
//    @Test
//    void authenticateUser_WrongPassword_ReturnsFalse() {
//        when(loginRepository.findUserByEmail("test@example.com")).thenReturn(mockUser);
//
//        boolean result = loginService.authenticateUser("test@example.com", "wrongpassword");
//
//        assertFalse(result, "Authentication should fail for incorrect password");
//    }
//
//    /**
//     * Tests authentication with a non-existent email.
//     * The expected result is false.
//     */
//    @Test
//    void authenticateUser_NonExistentEmail_ReturnsFalse() {
//        when(loginRepository.findUserByEmail("unknown@example.com")).thenReturn(null);
//
//        boolean result = loginService.authenticateUser("unknown@example.com", "password123");
//
//        assertFalse(result, "Authentication should fail for non-existent email");
//    }
//
//    /**
//     * Tests retrieving a user by email when the user exists.
//     * The expected result is a non-null LoginModel object with a matching email.
//     */
//    @Test
//    void getUserByEmail_ExistingUser_ReturnsUser() {
//        when(loginRepository.findUserByEmail("test@example.com")).thenReturn(mockUser);
//
//        LoginModel result = loginService.findUserByEmail("test@example.com");
//
//        assertNotNull(result, "User should be found");
//        assertEquals("test@example.com", result.getEmailAddress(), "Emails should match");
//    }
//
//    /**
//     * Tests retrieving a user by email when the user does not exist.
//     * The expected result is null.
//     */
//    @Test
//    void getUserByEmail_NonExistentUser_ReturnsNull() {
//        when(loginRepository.findUserByEmail("unknown@example.com")).thenReturn(null);
//
//        LoginModel result = loginService.findUserByEmail("unknown@example.com");
//
//        assertNull(result, "User should not be found");
//    }
//
//    /**
//     * Tests retrieving all users from the database.
//     * The expected result is a non-empty list of users.
//     */
//    @Test
//    void getAllUsers_ReturnsUserList() {
//        List<LoginModel> mockUsers = Arrays.asList(
//                new LoginModel("user1@example.com", BCrypt.hashpw("pass1", BCrypt.gensalt())),
//                new LoginModel("user2@example.com", BCrypt.hashpw("pass2", BCrypt.gensalt()))
//        );
//
//        when(loginRepository.getAllUsers()).thenReturn(mockUsers);
//
//        List<LoginModel> result = loginService.getAllUsers();
//
//        assertNotNull(result, "The returned user list should not be null");
//        assertEquals(2, result.size(), "The user list should contain two users");
//    }
//}
