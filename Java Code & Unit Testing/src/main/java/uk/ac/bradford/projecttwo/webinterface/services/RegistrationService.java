package uk.ac.bradford.projecttwo.webinterface.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import uk.ac.bradford.projecttwo.webinterface.models.RegistrationModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.RegistrationRepositoryImpl;
import org.springframework.stereotype.Service;
import uk.ac.bradford.projecttwo.webinterface.security.Encryptor;

/**
 * Service class for handling user registration and authentication.
 * This class interacts with the RegistrationRepository to store and retrieve user data securely.
 */
@Service
public class RegistrationService {

    // Repository instance for handling user database operations
    private final RegistrationRepositoryImpl registrationRepositoryImpl;

    // Encryptor for securely hashing passwords (Autowired for dependency injection)
    @Autowired
    private Encryptor encryptor;

    // Password encoder instance for hashing and verifying passwords
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Constructor-based dependency injection for the RegistrationRepository.
     *
     * @param registrationRepositoryImpl The repository responsible for managing user data.
     */
    public RegistrationService(RegistrationRepositoryImpl registrationRepositoryImpl) {
        this.registrationRepositoryImpl = registrationRepositoryImpl;
    }

    /**
     * Registers a new user in the system.
     *
     * @param user The RegistrationModel object containing user details.
     * @return true if the user is successfully registered, false if the email already exists.
     */
    public boolean registerUser(RegistrationModel user) {
        // Check if user with the given email already exists
        if (registrationRepositoryImpl.findUserByEmail(user.getEmailAddress()) != null) {
            System.out.println("Email Already Exists!"); // Consider replacing this with proper logging
            return false;
        }

        // Hash the password before saving the user
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save user in the database
        return registrationRepositoryImpl.registerUser(user);
    }

    /**
     * Authenticates a user by verifying their email and password.
     *
     * @param emailAddress The email address of the user.
     * @param password The plaintext password provided for authentication.
     * @return true if authentication is successful, false otherwise.
     */
    public boolean authenticateUser(String emailAddress, String password) {
        // Retrieve user by email
        RegistrationModel user = registrationRepositoryImpl.findUserByEmail(emailAddress);

        // Verify the password against the hashed password stored in the database
        if (user != null) {
            return BCrypt.checkpw(password, user.getPassword());
        }

        return false;
    }
}
