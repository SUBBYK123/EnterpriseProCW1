package uk.ac.bradford.projecttwo.webinterface.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.bradford.projecttwo.webinterface.models.ResetPasswordModel;
import uk.ac.bradford.projecttwo.webinterface.security.OTPUtil;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class ResetPasswordServiceImpl implements ResetPasswordService {

    private final Map<String, ResetPasswordModel> otpStorage = new HashMap<>();

    @Autowired
    private EmailService emailService;

    @Autowired
    private LoginServiceImpl loginService;


    @Override
    public void generateAndSendOtp(String emailAddress) throws Exception {
    String otp = OTPUtil.generateOTP();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(15);

        ResetPasswordModel resetToken = new ResetPasswordModel(emailAddress,otp,expiryTime);
        otpStorage.put(emailAddress, resetToken);

        emailService.sendOtpEmail(emailAddress, otp);

    }

    @Override
    public boolean verifyOtp(String emailAddress, String otp) {
        ResetPasswordModel token = otpStorage.get(emailAddress);

        if (token != null && !token.isExpired() && token.getOtp().equals(otp)){
            otpStorage.remove(emailAddress);
            return true;
        }
        return false;
    }

    @Override
    public boolean resetPassword(String emailAddress, String otp, String newPassword) {
        ResetPasswordModel token = otpStorage.get(emailAddress);

        if (token != null && token.getOtp().equals(otp) && !token.isExpired()){
            loginService.updateUserPassword(emailAddress,newPassword);
            otpStorage.remove(emailAddress);
            return true;
        }


        return false;
    }
}
