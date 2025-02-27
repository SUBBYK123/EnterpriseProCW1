package uk.ac.bradford.projecttwo.webinterface.repositories;

import uk.ac.bradford.projecttwo.webinterface.models.LoginModel;

import java.util.List;

public interface LoginRepository {
    boolean loginUser(LoginModel loginModel);
    LoginModel findUserByEmail(String email);
    List<LoginModel> getAllUsers();
}
