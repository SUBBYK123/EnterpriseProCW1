package uk.ac.bradford.projecttwo.webinterface.services;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import uk.ac.bradford.projecttwo.webinterface.models.LoginModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.UserRepositoryImpl;

import java.util.List;

/**
 * Implementation of the LoginService interface.
 * This class handles user authentication and retrieval from the database.
 */
@Service
public class LoginServiceImpl implements LoginService {

    // Repository for accessing user data
    private final UserRepositoryImpl loginRepository;

    /**
     * Constructor-based dependency injection for UserRepositoryImpl.
     *
     * @param loginRepository The repository responsible for retrieving user authentication data.
     */
    public LoginServiceImpl(UserRepositoryImpl loginRepository) {
        this.loginRepository = loginRepository;
    }

    /**
     * Authenticates a user by verifying the provided email and password.
     *
     * @param email The email address of the user.
     * @param password The plaintext password provided by the user.
     * @return true if authentication is successful, false otherwise.
     */
    @Override
    public boolean authenticateUser(String email, String password) {
        LoginModel user = loginRepository.findUserByEmail(email);
        if (user != null) {
            // Verifies the provided password against the hashed password stored in the database
            return BCrypt.checkpw(password, user.getPassword());
        }
        return false;
    }

    /**
     * Finds a user by their email address.
     *
     * @param email The email address to search for.
     * @return A LoginModel object containing user authentication details if found, otherwise null.
     */
    @Override
    public LoginModel findUserByEmail(String email) {
        return loginRepository.findUserByEmail(email);
    }

    /**
     * Retrieves a list of all users.
     *
     * @return A List of LoginModel objects representing all registered users.
     */
    @Override
    public List<LoginModel> getAllUsers() {
        return loginRepository.getAllUsers();
    }
}
