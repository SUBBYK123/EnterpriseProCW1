package uk.ac.bradford.projecttwo.webinterface.models;

import java.time.LocalDateTime;

/**
 * Represents the metadata for a dataset in the system.
 * This model contains information about the dataset's name, department,
 * the user who uploaded it, the role associated with the dataset,
 * upload date, and its approval/request status.
 */
public class DatasetMetadataModel {

    // Unique identifier for the dataset
    public int id;

    // Name of the dataset
    private String datasetName;

    // Department associated with the dataset
    private String department;

    // User who uploaded the dataset
    private String uploadedBy;

    // Role associated with the dataset (e.g., admin, user)
    private String role;

    // Date and time the dataset was uploaded
    private LocalDateTime uploadDate;

    // Approval status of the dataset (approved or not)
    private boolean approved;

    // Request status of the dataset (requested or not)
    private boolean requested;

    /**
     * Gets the unique identifier for the dataset.
     *
     * @return The unique dataset ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the dataset.
     *
     * @param id The unique dataset ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of the dataset.
     *
     * @return The name of the dataset.
     */
    public String getDatasetName() {
        return datasetName;
    }

    /**
     * Sets the name of the dataset.
     *
     * @param datasetName The name of the dataset to set.
     */
    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    /**
     * Gets the department associated with the dataset.
     *
     * @return The department of the dataset.
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the department associated with the dataset.
     *
     * @param department The department to set for the dataset.
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Gets the user who uploaded the dataset.
     *
     * @return The email of the user who uploaded the dataset.
     */
    public String getUploadedBy() {
        return uploadedBy;
    }

    /**
     * Sets the user who uploaded the dataset.
     *
     * @param uploadedBy The email of the user to set.
     */
    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    /**
     * Gets the date and time the dataset was uploaded.
     *
     * @return The upload date and time of the dataset.
     */
    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    /**
     * Sets the date and time the dataset was uploaded.
     *
     * @param uploadDate The upload date and time to set.
     */
    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    /**
     * Gets the role associated with the dataset (e.g., admin, user).
     *
     * @return The role associated with the dataset.
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role associated with the dataset.
     *
     * @param role The role to set for the dataset.
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Checks whether the dataset is approved.
     *
     * @return True if the dataset is approved, false otherwise.
     */
    public boolean isApproved() {
        return approved;
    }

    /**
     * Sets the approval status of the dataset.
     *
     * @param approved The approval status to set (true or false).
     */
    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    /**
     * Checks whether a request has been made for the dataset.
     *
     * @return True if the dataset is requested, false otherwise.
     */
    public boolean isRequested() {
        return requested;
    }

    /**
     * Sets the request status of the dataset.
     *
     * @param requested The request status to set (true or false).
     */
    public void setRequested(boolean requested) {
        this.requested = requested;
    }
}
