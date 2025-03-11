package uk.ac.bradford.projecttwo.webinterface.services;

/**
 * Interface for handling password reset functionality, including OTP generation,
 * verification, and password resetting.
 */
public interface ResetPasswordService {

    /**
     * Generates an OTP and sends it to the specified email address.
     * @param emailAddress The email address to which the OTP should be sent.
     * @throws Exception If an error occurs during OTP generation or email sending.
     */
    void generateAndSendOtp(String emailAddress) throws Exception;

    /**
     * Verifies whether the provided OTP is valid for the given email address.
     * @param emailAddress The email address associated with the OTP.
     * @param otp The OTP to be verified.
     * @return true if the OTP is valid, false otherwise.
     */
    boolean verifyOtp(String emailAddress, String otp);

    /**
     * Resets the password if the provided OTP is valid.
     * @param emailAddress The email address for which the password is being reset.
     * @param otp The OTP required for authentication.
     * @param newPassword The new password to be set.
     * @return true if the password is successfully reset, false otherwise.
     */
    boolean resetPassword(String emailAddress, String otp, String newPassword);
}
