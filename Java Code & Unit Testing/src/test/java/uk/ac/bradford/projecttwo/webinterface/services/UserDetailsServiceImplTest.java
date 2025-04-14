package uk.ac.bradford.projecttwo.webinterface.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import uk.ac.bradford.projecttwo.webinterface.models.LoginModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link UserDetailsServiceImpl}, which is used by Spring Security
 * to load user details for authentication.
 */
class UserDetailsServiceImplTest {

    private UserRepository userRepository;
    private UserDetailsServiceImpl userDetailsService;

    /**
     * Sets up mocked {@link UserRepository} and injects it into the UserDetailsServiceImpl before each test.
     */
    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userDetailsService = new UserDetailsServiceImpl(userRepository);
    }

    /**
     * Verifies that when a valid email is provided, the user details are correctly returned,
     * including username, password, and role-based authorities.
     */
    @Test
    void testLoadUserByUsername_UserFound() {
        // Arrange
        LoginModel mockUser = new LoginModel("test@example.com", "hashedpassword", "ROLE_USER");
        when(userRepository.findUserByEmail("test@example.com")).thenReturn(mockUser);

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");

        // Assert
        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("hashedpassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));

        verify(userRepository, times(1)).findUserByEmail("test@example.com");
    }

    /**
     * Verifies that a {@link UsernameNotFoundException} is thrown when no user is found with the provided email.
     */
    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Arrange
        when(userRepository.findUserByEmail("missing@example.com")).thenReturn(null);

        // Assert + Act
        assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername("missing@example.com"));

        verify(userRepository, times(1)).findUserByEmail("missing@example.com");
    }
}
