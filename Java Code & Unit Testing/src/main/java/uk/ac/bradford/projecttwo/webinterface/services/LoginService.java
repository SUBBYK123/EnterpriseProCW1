package uk.ac.bradford.projecttwo.webinterface.services;

import uk.ac.bradford.projecttwo.webinterface.models.LoginModel;

import java.util.List;

/**
 * The LoginService interface defines the contract for user authentication-related operations.
 * Implementations of this interface will handle user authentication and retrieval.
 */
public interface LoginService {

    /**
     * Authenticates a user based on their email and password.
     *
     * @param email The email address of the user.
     * @param password The password provided for authentication.
     * @return true if authentication is successful, false otherwise.
     */
    boolean authenticateUser(String email, String password);

    /**
     * Finds a user by their email address.
     *
     * @param email The email address to search for.
     * @return A LoginModel object containing user authentication details if found, otherwise null.
     */
    LoginModel findUserByEmail(String email);

    /**
     * Retrieves a list of all users.
     *
     * @return A List of LoginModel objects representing all registered users.
     */
    List<LoginModel> getAllUsers();
}
