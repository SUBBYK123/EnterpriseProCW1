package uk.ac.bradford.projecttwo.webinterface.models;

/**
 * The LoginModel class represents a user login model
 * that holds user authentication details such as email address and password.
 */
public class LoginModel {
    // Unique identifier for the user
    private int userId;

    // Email address of the user (used for login)
    private String emailAddress;

    // Password of the user (used for authentication)
    private String password;

    private String role;

    /**
     * Constructor to initialize the login model with email and password.
     *
     * @param emailAddress The email address of the user.
     * @param password The password of the user.
     */
    public LoginModel(String emailAddress, String password, String role) {
        this.emailAddress = emailAddress;
        this.password = password;
        this.role = role;
    }

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

    /**
     * Gets the password of the user.
     *
     * @return The password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password The new password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the user ID.
     *
     * @return The user ID.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the user ID.
     *
     * @param userId The new user ID to set.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
