package uk.ac.bradford.projecttwo.webinterface.interfaces;

import uk.ac.bradford.projecttwo.webinterface.models.RegistrationModel;

import java.util.List;


public interface RegistrationInterface {

    boolean registerUser(RegistrationModel registerUser);
    RegistrationModel findUserByEmail(String email);
    List<RegistrationModel> getAllUsers();



}
