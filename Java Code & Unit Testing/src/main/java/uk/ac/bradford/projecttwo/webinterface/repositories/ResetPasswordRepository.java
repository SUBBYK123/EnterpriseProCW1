package uk.ac.bradford.projecttwo.webinterface.repositories;

import uk.ac.bradford.projecttwo.webinterface.models.ResetPasswordModel;

import java.util.List;

/**
 * Repository interface for handling password reset-related operations.
 */
public interface ResetPasswordRepository {

    /**
     * Checks whether a user exists in the system by their email address.
     *
     * @param email The email address to verify.
     * @return true if the user exists, false otherwise.
     */
    boolean authenticateUser(String email);

    /**
     * Retrieves the user details associated with the specified email.
     *
     * @param email The email address of the user.
     * @return A {@link ResetPasswordModel} containing user information if found; null otherwise.
     */
    ResetPasswordModel findUserByEmail(String email);

    /**
     * Retrieves all users registered in the system.
     *
     * @return A list of {@link ResetPasswordModel} objects representing all users.
     */
    List<ResetPasswordModel> getAllUsers();
}
