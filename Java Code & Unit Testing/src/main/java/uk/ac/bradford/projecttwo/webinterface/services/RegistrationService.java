package uk.ac.bradford.projecttwo.webinterface.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import uk.ac.bradford.projecttwo.webinterface.models.RegistrationModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.RegistrationRepositoryImpl;
import org.springframework.stereotype.Service;
import uk.ac.bradford.projecttwo.webinterface.security.Encryptor;

@Service
public class RegistrationService {

    private final RegistrationRepositoryImpl registrationRepositoryImpl;

    @Autowired
    private Encryptor encryptor;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public RegistrationService(RegistrationRepositoryImpl registrationRepositoryImpl) {
        this.registrationRepositoryImpl = registrationRepositoryImpl;
    }

    public boolean registerUser(RegistrationModel user) {
        // Check if user already exists
        if (registrationRepositoryImpl.findUserByEmail(user.getEmailAddress()) != null) {
            System.out.println("Email Already Exists!");
            return false;
        }

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
