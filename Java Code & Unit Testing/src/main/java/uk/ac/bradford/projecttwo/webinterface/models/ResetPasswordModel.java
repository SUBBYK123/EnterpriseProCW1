package uk.ac.bradford.projecttwo.webinterface.models;

/**
 * The ResetPasswordModel class represents the model for resetting a user's password.
 * It contains only the user's email address, which is used to initiate the password reset process.
 */
public class ResetPasswordModel {
    // The email address associated with the user's account
    private String emailAddress;

    /**
     * Gets the email address of the user.
     *
     * @return The email address of the user.
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets the email address of the user.
     *
     * @param emailAddress The new email address to set.
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
