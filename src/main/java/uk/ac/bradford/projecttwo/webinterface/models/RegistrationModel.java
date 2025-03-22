package uk.ac.bradford.projecttwo.webinterface.models;

import org.springframework.security.crypto.bcrypt.BCrypt;
import uk.ac.bradford.projecttwo.webinterface.security.Encryptor;

/**
 * The RegistrationModel class represents user registration details.
 * It includes personal details such as first name, last name, email address,
 * password, department, and a unique user ID.
 */
public class RegistrationModel {
    // Unique identifier for the user
    private int userId;

    // First name of the user
    private String firstName;

    // Last name of the user
    private String lastName;

    // Email address of the user (used for registration and login)
    private String emailAddress;

    // Encrypted password of the user
    private String password;

    // Department to which the user belongs
    private String department;

    /**
     * Gets the first name of the user.
     *
     * @return The first name of the user.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstName The new first name to set.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the user.
     *
     * @return The last name of the user.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastName The new last name to set.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
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
     * Gets the encrypted password of the user.
     *
     * @return The encrypted password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets and encrypts the password of the user before storing it.
     *
     * @param password The new password to encrypt and set.
     */
    public void setPassword(String password) {
        Encryptor encryptor = new Encryptor(); // Creating an instance of Encryptor
        this.password = encryptor.encryptString(password); // Encrypting and storing the password
    }

    /**
     * Gets the department of the user.
     *
     * @return The department of the user.
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the department of the user.
     *
     * @param department The new department to set.
     */
    public void setDepartment(String department) {
        this.department = department;
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
}
