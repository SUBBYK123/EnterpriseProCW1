package uk.ac.bradford.projecttwo.webinterface.repositories;

import uk.ac.bradford.projecttwo.webinterface.models.RegistrationModel;

import java.util.List;


public interface RegistrationRepository {

    boolean registerUser(RegistrationModel registerUser);
    RegistrationModel findUserByEmail(String email);
    List<RegistrationModel> getAllUsers();



}
