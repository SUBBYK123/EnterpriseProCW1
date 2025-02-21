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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationServicesTest {

    @Mock
    private RegistrationRepositoryImpl registrationRepository;

    @InjectMocks
    private RegistrationServices registrationServices;

    private RegistrationModel user;

    @BeforeEach
    void setUp(){
        user = new RegistrationModel();
        user.setEmailAddress("test@example.com");
        user.setPassword("password123");
    }

    @Test
    void testRegisterUser_Success(){
        when(registrationRepository.findUserByEmail(user.getEmailAddress())).thenReturn(null);
        when(registrationRepository.registerUser(any(RegistrationModel.class))).thenReturn(true);

        boolean result = registrationServices.registerUser(user);

        assertTrue(result);
        verify(registrationRepository).registerUser(any(RegistrationModel.class));

    }

    @Test
    void testRegisterUser_UserAlreadyExists(){
        when(registrationRepository.findUserByEmail(user.getEmailAddress())).thenReturn(user);

        boolean result = registrationServices.registerUser(user);

        assertFalse(result);
        verify(registrationRepository, never()).registerUser(any(RegistrationModel.class));

    }

    @Test //Still Need to get this test to work successfully
    void testAuthenticateUser_Success() {
        String rawPassword = "password123";
        String fixedHashedPassword = "$2a$10$Dow0rZH2hP0n5qyrOqTpUeFKr2eb3Z7p/64rwE/o1CnMy6vlrJb7G"; // Hash of "password123"

        user.setPassword(fixedHashedPassword);
        when(registrationRepository.findUserByEmail(user.getEmailAddress())).thenReturn(user);

        boolean result = registrationServices.authenticateUser("test@example.com", rawPassword);

        assertTrue(result, "Authentication should succeed for correct password");
    }


    @Test
    void testAuthenticateUser_UserNotFound(){
        when(registrationRepository.findUserByEmail("test@example.com")).thenReturn(null);

        boolean result = registrationServices.authenticateUser("test@example.com","password123");

        assertFalse(result);
    }

    @Test
    void testAuthenticateUser_IncorrectPassword(){
        String hashedPassword = BCrypt.hashpw("password123",BCrypt.gensalt());
        user.setPassword(hashedPassword);
        when(registrationRepository.findUserByEmail("test@example.com")).thenReturn(user);

        boolean result = registrationServices.authenticateUser("test@example.com","wrongpassword");

        assertFalse(result);
    }


}
