package uk.ac.bradford.projecttwo.webinterface.models;

import java.util.List;

/**
 * Represents a permission request model for requesting access to specific datasets or roles.
 * It holds the user's details, requested role, department, accessible datasets, and the current status of the request.
 */
public class PermissionRequestModel {

    // Unique identifier for the permission request
    private int requestId;

    // Email address of the user making the request
    private String emailAddress;

    // First name of the user making the request
    private String firstName;

    // Last name of the user making the request
    private String lastName;

    // The role the user is requesting
    private String requestedRole;

    // The department of the user making the request
    private String department;

    // List of datasets that the user wants access to
    private List<String> accessibleDatasets;

    // Status of the permission request (e.g., PENDING, APPROVED, DENIED)
    private String status;

    /**
     * Gets the unique identifier for the permission request.
     *
     * @return The request ID.
     */
    public int getRequestId() {
        return requestId;
    }

    /**
     * Sets the unique identifier for the permission request.
     *
     * @param requestId The request ID to set.
     */
    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    /**
     * Gets the email address of the user making the request.
     *
     * @return The user's email address.
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets the email address of the user making the request.
     *
     * @param emailAddress The email address to set.
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Gets the first name of the user making the request.
     *
     * @return The user's first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user making the request.
     *
     * @param firstName The first name to set.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the requested role for the user.
     *
     * @return The requested role.
     */
    public String getRequestedRole() {
        return requestedRole;
    }

    /**
     * Sets the requested role for the user.
     *
     * @param requestedRole The role to set.
     */
    public void setRequestedRole(String requestedRole) {
        this.requestedRole = requestedRole;
    }

    /**
     * Gets the department of the user making the request.
     *
     * @return The user's department.
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the department of the user making the request.
     *
     * @param department The department to set.
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Gets the list of datasets the user is requesting access to.
     *
     * @return The list of datasets.
     */
    public List<String> getAccessibleDatasets() {
        return accessibleDatasets;
    }

    /**
     * Sets the list of datasets the user is requesting access to.
     *
     * @param accessibleDatasets The list of datasets to set.
     */
    public void setAccessibleDatasets(List<String> accessibleDatasets) {
        this.accessibleDatasets = accessibleDatasets;
    }

    /**
     * Gets the status of the permission request.
     *
     * @return The status of the request (e.g., PENDING, APPROVED, DENIED).
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the permission request.
     *
     * @param status The status to set.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the last name of the user making the request.
     *
     * @return The user's last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user making the request.
     *
     * @param lastName The last name to set.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
