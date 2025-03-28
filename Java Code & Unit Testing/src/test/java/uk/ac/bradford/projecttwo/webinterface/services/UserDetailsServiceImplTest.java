 package uk.ac.bradford.projecttwo.webinterface.services;

 import org.junit.jupiter.api.BeforeEach;
 import org.junit.jupiter.api.Test;
 import org.mockito.InjectMocks;
 import org.mockito.Mock;
 import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import uk.ac.bradford.projecttwo.webinterface.models.LoginModel;
 import uk.ac.bradford.projecttwo.webinterface.repositories.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
 import static org.mockito.Mockito.*;

/**
 * Unit tests for the UserDetailsServiceImpl class.
 */
 class UserDetailsServiceImplTest {

     @Mock
     private UserRepository mockUserRepository;

     @InjectMocks
     private UserDetailsServiceImpl userDetailsService;

     /**
      * Initializes mocks before each test case.
      */
     @BeforeEach
     void setUp() {
         MockitoAnnotations.openMocks(this); // Initialize mocks
     }

     /**
      * Tests the loadUserByUsername method when the user exists in the repository.
      * Ensures that the returned UserDetails object matches the expected values.
      */
     @Test
     void testLoadUserByUsername_UserExists() {
         String email = "test@example.com";
         String hashedPassword = "$2a$12$somethinghashed";

         // Mock repository behavior
         when(mockUserRepository.findUserByEmail(email)).thenReturn(new LoginModel(email, hashedPassword));

         // Load user details
         UserDetails userDetails = userDetailsService.loadUserByUsername(email);

         // Assertions to verify correct behavior
         assertNotNull(userDetails);
         assertEquals(email, userDetails.getUsername());
         assertEquals(hashedPassword, userDetails.getPassword());
     }

     /**
      * Tests the loadUserByUsername method when the user does not exist.
      * Ensures that a UsernameNotFoundException is thrown.
      */
     @Test
     void testLoadUserByUsername_UserNotFound() {
         // Mock repository returning null for a non-existent user
         when(mockUserRepository.findUserByEmail("nonexistent@example.com")).thenReturn(null);

         // Assert that the method throws an exception for a non-existent user
         assertThrows(UsernameNotFoundException.class, () -> {
             userDetailsService.loadUserByUsername("nonexistent@example.com");
         });
     }
 }