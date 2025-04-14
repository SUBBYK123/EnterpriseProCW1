package uk.ac.bradford.projecttwo.webinterface.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uk.ac.bradford.projecttwo.webinterface.models.LoginModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.UserRepository;

import java.util.Collections;

/**
 * Service implementation of {@link UserDetailsService} for Spring Security.
 * Loads user-specific data from the database for authentication.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructs a new instance of {@code UserDetailsServiceImpl} with the provided {@link UserRepository}.
     *
     * @param userRepository the repository used to retrieve user data
     */
    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads the user from the database by their email address (used as username).
     *
     * @param username the user's email address
     * @return a {@link UserDetails} object containing the user credentials and authorities
     * @throws UsernameNotFoundException if no user is found with the given email
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginModel loginModel = userRepository.findUserByEmail(username);
        if (loginModel == null) {
            throw new UsernameNotFoundException(username);
        }

        return new User(
                loginModel.getEmailAddress(),
                loginModel.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(loginModel.getRole()))
        );
    }
}
