package uk.ac.bradford.projecttwo.webinterface.services;

import org.springframework.security.crypto.bcrypt.BCrypt;
import uk.ac.bradford.projecttwo.webinterface.models.RegistrationModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.RegistrationRepository;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServices {

    private final RegistrationRepository registrationRepository;


    public RegistrationServices(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    public boolean registerUser(RegistrationModel user) {
        // Check if user already exists
        if (registrationRepository.findUserByEmail(user.getEmailAddress()) != null) {
            System.out.println("User Already Exists!");
            return false;
        }

        // Hash password before storing
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);

        // Save user
        return registrationRepository.registerUser(user);
    }

    public boolean authenticateUser(String emailAddress, String password) {
        // Find user by email
        RegistrationModel user = registrationRepository.findUserByEmail(emailAddress);

        // Check password match
        if (user != null) {
            return BCrypt.checkpw(password, user.getPassword());
        }

        return false;
    }
}
