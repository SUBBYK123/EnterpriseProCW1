package uk.ac.bradford.projecttwo.webinterface.models;

import java.time.LocalDateTime;

/**
 * Represents a dataset access request made by a user.
 * This model contains information such as the dataset name, the user requesting access,
 * their department, role, request date, and the current status of the request.
 */
public class DatasetAccessRequestModel {

    // Unique identifier for the dataset access request
    private int id;

    // Name of the dataset for which access is requested
    private String datasetName;

    // Email of the user who requested access to the dataset
    private String requestedBy;

    // Department of the user requesting access
    private String department;

    // Role of the user requesting access
    private String role;

    // Date and time when the access request was made
    private LocalDateTime requestDate;

    // Current status of the request (e.g., PENDING, APPROVED, DENIED)
    private String status;

    /**
     * Gets the unique identifier for the dataset access request.
     *
     * @return The unique dataset access request ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the dataset access request.
     *
     * @param id The unique dataset access request ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of the dataset for which access is requested.
     *
     * @return The name of the dataset.
     */
    public String getDatasetName() {
        return datasetName;
    }

    /**
     * Sets the name of the dataset for which access is requested.
     *
     * @param datasetName The name of the dataset to set.
     */
    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    /**
     * Gets the email address of the user who requested access.
     *
     * @return The email address of the user requesting access.
     */
    public String getRequestedBy() {
        return requestedBy;
    }

    /**
     * Sets the email address of the user who requested access.
     *
     * @param requestedBy The email address to set.
     */
    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    /**
     * Gets the department of the user requesting access.
     *
     * @return The department of the user.
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the department of the user requesting access.
     *
     * @param department The department to set.
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Gets the role of the user requesting access.
     *
     * @return The role of the user requesting access.
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role of the user requesting access.
     *
     * @param role The role to set.
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Gets the date and time when the dataset access request was made.
     *
     * @return The date and time of the request.
     */
    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    /**
     * Sets the date and time when the dataset access request was made.
     *
     * @param requestDate The request date and time to set.
     */
    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    /**
     * Gets the current status of the dataset access request.
     *
     * @return The status of the request (e.g., PENDING, APPROVED, DENIED).
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the current status of the dataset access request.
     *
     * @param status The status to set (e.g., PENDING, APPROVED, DENIED).
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
