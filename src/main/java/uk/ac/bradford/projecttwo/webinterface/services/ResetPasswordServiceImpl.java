package uk.ac.bradford.projecttwo.webinterface.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.bradford.projecttwo.webinterface.models.ResetPasswordModel;
import uk.ac.bradford.projecttwo.webinterface.security.OTPUtil;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the ResetPasswordService for handling OTP generation, verification,
 * and password resetting functionality.
 */
@Service
public class ResetPasswordServiceImpl implements ResetPasswordService {

    // Storage for OTPs mapped to email addresses
    private final Map<String, ResetPasswordModel> otpStorage = new HashMap<>();

    @Autowired
    private EmailService emailService;

    @Autowired
    private LoginServiceImpl loginService;

    /**
     * Generates an OTP, stores it, and sends it to the specified email address.
     * @param emailAddress The recipient's email address.
     * @throws Exception If an error occurs while generating or sending the OTP.
     */
    @Override
    public void generateAndSendOtp(String emailAddress) throws Exception {
        String otp = OTPUtil.generateOTP();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(15);

        // Store the OTP with expiry time
        ResetPasswordModel resetToken = new ResetPasswordModel(emailAddress, otp, expiryTime);
        otpStorage.put(emailAddress, resetToken);

        // Send the OTP via email
        emailService.sendOtpEmail(emailAddress, otp);
    }

    /**
     * Verifies whether the provided OTP is valid for the given email address.
     * @param emailAddress The email address associated with the OTP.
     * @param otp The OTP to be verified.
     * @return true if the OTP is valid and not expired, false otherwise.
     */
    @Override
    public boolean verifyOtp(String emailAddress, String otp) {
        ResetPasswordModel token = otpStorage.get(emailAddress);

        if (token != null && !token.isExpired() && token.getOtp().equals(otp)) {
            otpStorage.remove(emailAddress); // Remove OTP after successful verification
            return true;
        }
        return false;
    }

    /**
     * Resets the user's password if the provided OTP is valid.
     * @param emailAddress The email address associated with the OTP.
     * @param otp The OTP for authentication.
     * @param newPassword The new password to be set.
     * @return true if the password reset is successful, false otherwise.
     */
    @Override
    public boolean resetPassword(String emailAddress, String otp, String newPassword) {
        ResetPasswordModel token = otpStorage.get(emailAddress);

        if (token != null && token.getOtp().equals(otp) && !token.isExpired()) {
            loginService.updateUserPassword(emailAddress, newPassword); // Update the user's password
            otpStorage.remove(emailAddress); // Remove OTP after successful password reset
            return true;
        }
        return false;
    }
}
