package uk.ac.bradford.projecttwo.webinterface.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EncryptorTest {

    private final Encryptor encryptor = new Encryptor();

    @Test
    public void testEncryptString(){
        String rawPassword = "password";
        String encryptedPassword = encryptor.encryptString(rawPassword);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(rawPassword + encryptedPassword);
        assertTrue(encoder.matches(rawPassword,encryptedPassword));
    }

}
