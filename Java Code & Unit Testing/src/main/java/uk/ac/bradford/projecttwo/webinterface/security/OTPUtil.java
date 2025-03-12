package uk.ac.bradford.projecttwo.webinterface.security;

import java.security.SecureRandom;

public class OTPUtil {
    private static final SecureRandom random = new SecureRandom();

    public static String generateOTP(){
        return String.format("%06d",random.nextInt(1000000));
    }
}
