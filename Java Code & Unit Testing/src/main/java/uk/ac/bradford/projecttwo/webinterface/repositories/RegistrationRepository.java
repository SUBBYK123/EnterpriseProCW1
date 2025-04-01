package uk.ac.bradford.projecttwo.webinterface.repositories;

import uk.ac.bradford.projecttwo.webinterface.models.RegistrationModel;

import java.util.List;

/**
 * The RegistrationRepository interface defines the contract for user registration-related database operations.
 * Implementations of this interface will handle storing, retrieving, and managing user registration data.
 */
public interface RegistrationRepository {

    /**
     * Registers a new user in the system.
     *
     * @param registerUser The RegistrationModel object containing user details.
     * @return true if registration is successful, false otherwise.
     */
    boolean registerUser(RegistrationModel registerUser);

    /**
     * Finds a user by their email address.
     *
     * @param email The email address to search for.
     * @return A RegistrationModel object containing user details if found, otherwise null.
     */
    RegistrationModel findUserByEmail(String email);

    /**
     * Retrieves a list of all registered users.
     *
     * @return A List of RegistrationModel objects representing all registered users.
     */
    List<RegistrationModel> getAllUsers();

    boolean savePendingUser(RegistrationModel user);
}
