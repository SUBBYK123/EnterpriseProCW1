package uk.ac.bradford.projecttwo.webinterface.models;

import org.springframework.security.crypto.bcrypt.BCrypt;


public class RegistrationModel {
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String password;
    private String Department;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password,"$2a$10$Dow0rZH2hP0n5qyrOqTpUeFKr2eb3Z7p/64rwE/o1CnMy6vlrJb7G");
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }
}
