package uk.ac.bradford.projecttwo.webinterface.models;

import java.time.LocalDateTime;

/**
 * The ResetPasswordModel class represents the model for resetting a user's password.
 * It contains only the user's email address, which is used to initiate the password reset process.
 */
public class ResetPasswordModel {
    // The email address associated with the user's account
    private long id;
    private String emailAddress;
    private String otp;
    private LocalDateTime expiryTime;

    public ResetPasswordModel(String emailAddress, String otp, LocalDateTime expiryTime){
        this.emailAddress = emailAddress;
        this.expiryTime = expiryTime;
        this.otp = otp;
    }

    public boolean isExpired(){
        return LocalDateTime.now().isAfter(this.expiryTime);
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }
}
