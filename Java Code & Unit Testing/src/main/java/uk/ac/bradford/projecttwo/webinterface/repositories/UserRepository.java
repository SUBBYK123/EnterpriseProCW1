package uk.ac.bradford.projecttwo.webinterface.repositories;

import uk.ac.bradford.projecttwo.webinterface.models.LoginModel;

import java.util.List;

public interface UserRepository {
    LoginModel findUserByEmail(String email);
    List<LoginModel> getAllUsers();
}
