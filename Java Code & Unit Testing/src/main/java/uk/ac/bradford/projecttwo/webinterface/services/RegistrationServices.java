package uk.ac.bradford.projecttwo.webinterface.services;

import org.springframework.security.crypto.bcrypt.BCrypt;
import uk.ac.bradford.projecttwo.webinterface.models.RegistrationModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.RegistrationRepositoryImpl;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServices {

    private final RegistrationRepositoryImpl registrationRepositoryImpl;


    public RegistrationServices(RegistrationRepositoryImpl registrationRepositoryImpl) {
        this.registrationRepositoryImpl = registrationRepositoryImpl;
    }

    public boolean registerUser(RegistrationModel user) {
        // Check if user already exists
        if (registrationRepositoryImpl.findUserByEmail(user.getEmailAddress()) != null) {
            System.out.println("User Already Exists!");
            return false;
        }

        // Hash password before storing
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);

        // Save user
        return registrationRepositoryImpl.registerUser(user);
    }

    public boolean authenticateUser(String emailAddress, String password) {
        // Find user by email
        RegistrationModel user = registrationRepositoryImpl.findUserByEmail(emailAddress);

        // Check password match
        if (user != null) {
            return BCrypt.checkpw(password, user.getPassword());
        }

        return false;
    }
}
