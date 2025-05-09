package uk.ac.bradford.projecttwo.webinterface.repositories;


import uk.ac.bradford.projecttwo.webinterface.models.LoginModel;


import java.util.List;

/**
 * The UserRepository interface defines the contract for user authentication-related database operations.
 * Implementations of this interface will handle user retrieval and authentication processes.
 */
public interface UserRepository {

    /**
     * Finds a user by their email address.
     *
     * @param email The email address to search for.
     * @return A LoginModel object containing user authentication details if found, otherwise null.
     */
    LoginModel findUserByEmail(String email);

    /**
     * Retrieves a list of all users in the system.
     *
     * @return A List of LoginModel objects representing all registered users.
     */
    List<LoginModel> getAllUsers();


    void updateUserPassword(String email, String newPasswordHash);



}
