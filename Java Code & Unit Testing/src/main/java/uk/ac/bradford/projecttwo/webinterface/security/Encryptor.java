package uk.ac.bradford.projecttwo.webinterface.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class Encryptor {
        private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        public String encryptString(String input) {
            return passwordEncoder.encode(input);
        }
    }



