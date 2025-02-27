package uk.ac.bradford.projecttwo.webinterface.services;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import uk.ac.bradford.projecttwo.webinterface.models.LoginModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.LoginRepositoryImpl;

import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {
    private final LoginRepositoryImpl loginRepository;

    public LoginServiceImpl(LoginRepositoryImpl loginRepository) {
        this.loginRepository = loginRepository;
    }

    @Override
    public boolean authenticateUser(String email, String password) {
        LoginModel user = loginRepository.findUserByEmail(email);
        if (user != null){
            return BCrypt.checkpw(password,user.getPassword());
        }
        return false;
    }

    @Override
    public LoginModel findUserByEmail(String email) {
        return loginRepository.findUserByEmail(email);
    }

    @Override
    public List<LoginModel> getAllUsers() {
        return loginRepository.getAllUsers();
    }
}
