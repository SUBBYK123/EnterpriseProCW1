package uk.ac.bradford.projecttwo.webinterface.services;

import uk.ac.bradford.projecttwo.webinterface.models.LoginModel;

import java.util.List;

public interface LoginService {
    boolean authenticateUser(String email, String password);
    LoginModel findUserByEmail(String email);
    List<LoginModel> getAllUsers();
}
