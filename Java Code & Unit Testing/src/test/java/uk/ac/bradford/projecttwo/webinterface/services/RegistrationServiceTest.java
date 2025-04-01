package uk.ac.bradford.projecttwo.webinterface.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import uk.ac.bradford.projecttwo.webinterface.models.RegistrationModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.RegistrationRepositoryImpl;
import uk.ac.bradford.projecttwo.webinterface.security.Encryptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegistrationServiceTest {

    @Mock
    private RegistrationRepositoryImpl registrationRepository;

    @Mock
    private Encryptor encryptor; // Not directly used in your service logic but included for completeness

    @InjectMocks
    private RegistrationService registrationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_shouldReturnFalse_whenEmailAlreadyExists() {
        RegistrationModel existingUser = new RegistrationModel();
        existingUser.setEmailAddress("test@example.com");

        when(registrationRepository.findUserByEmail("test@example.com")).thenReturn(existingUser);

        RegistrationModel newUser = new RegistrationModel();
        newUser.setEmailAddress("test@example.com");
        newUser.setPassword("password123");

        boolean result = registrationService.registerUser(newUser);

        assertFalse(result);
        verify(registrationRepository, never()).registerUser(any());
    }

    @Test
    void registerUser_shouldRegisterSuccessfully_whenEmailIsUnique() {
        when(registrationRepository.findUserByEmail("unique@example.com")).thenReturn(null);
        when(registrationRepository.registerUser(any(RegistrationModel.class))).thenReturn(true);

        RegistrationModel newUser = new RegistrationModel();
        newUser.setEmailAddress("unique@example.com");
        newUser.setPassword("password123");

        boolean result = registrationService.registerUser(newUser);

        assertTrue(result);
        verify(registrationRepository).registerUser(argThat(user ->
                user.getEmailAddress().equals("unique@example.com") &&
                        !user.getPassword().equals("password123") // password should be encoded
        ));
    }

    @Test
    void authenticateUser_shouldReturnFalse_whenUserDoesNotExist() {
        when(registrationRepository.findUserByEmail("notfound@example.com")).thenReturn(null);

        boolean result = registrationService.authenticateUser("notfound@example.com", "password");

        assertFalse(result);
    }


    //Test isn't working atm
    @Test
    void authenticateUser_shouldReturnTrue_whenPasswordMatches() {
        String rawPassword = "mypassword";
        String hashedPassword = new BCryptPasswordEncoder().encode(rawPassword);

        RegistrationModel user = new RegistrationModel();
        user.setEmailAddress("test@example.com");
        user.setPassword(hashedPassword);

        when(registrationRepository.findUserByEmail("test@example.com")).thenReturn(user);

        boolean result = registrationService.authenticateUser("test@example.com", rawPassword);

        assertTrue(result);
    }

    @Test
    void authenticateUser_shouldReturnFalse_whenPasswordDoesNotMatch() {
        RegistrationModel user = new RegistrationModel();
        user.setEmailAddress("test@example.com");
        user.setPassword(new BCryptPasswordEncoder().encode("correctPassword"));

        when(registrationRepository.findUserByEmail("test@example.com")).thenReturn(user);

        boolean result = registrationService.authenticateUser("test@example.com", "wrongPassword");

        assertFalse(result);
    }
}
