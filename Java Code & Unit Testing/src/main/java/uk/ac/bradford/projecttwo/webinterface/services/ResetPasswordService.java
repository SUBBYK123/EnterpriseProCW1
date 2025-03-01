package uk.ac.bradford.projecttwo.webinterface.services;

import uk.ac.bradford.projecttwo.webinterface.models.ResetPasswordModel;

import java.util.List;

public interface ResetPasswordService {
    boolean authenticateUser(String email);
    ResetPasswordModel findUserByEmail(String email);
    List<ResetPasswordModel> getAllUsers();
}
