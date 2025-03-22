package uk.ac.bradford.projecttwo.webinterface.repositories;

import uk.ac.bradford.projecttwo.webinterface.models.ResetPasswordModel;

import java.util.List;

public interface ResetPasswordRepository {
    boolean authenticateUser(String email);
    ResetPasswordModel findUserByEmail(String email);
    List<ResetPasswordModel> getAllUsers();
}
