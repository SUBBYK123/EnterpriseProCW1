package uk.ac.bradford.projecttwo.webinterface.services;

public interface ResetPasswordService {
    void generateAndSendOtp(String emailAddress) throws Exception;
    boolean verifyOtp(String emailAddress, String otp);
    boolean resetPassword(String emailAddress, String otp, String newPassword);
}
