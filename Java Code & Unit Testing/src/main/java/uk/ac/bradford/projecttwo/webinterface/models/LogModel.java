package uk.ac.bradford.projecttwo.webinterface.models;

import java.time.LocalDateTime;

/**
 * Represents a log entry in the system.
 * This model is used to store details about actions performed by users, including
 * their email address, the action taken, the status of the action, and the timestamp of the log entry.
 */
public class LogModel {

    // Unique identifier for the log entry
    private int logId;

    // Email address of the user performing the action
    private String emailAddress;

    // Description of the action performed
    private String action;

    // Status of the action (e.g., SUCCESS, FAILED)
    private String status;

    // Timestamp when the log entry was created
    private LocalDateTime timeStamp;

    /**
     * Constructor to initialize a log entry with the given email address, action, and status.
     * The timestamp is automatically set to the current time when the log is created.
     *
     * @param emailAddress The email address of the user.
     * @param action The action performed by the user.
     * @param status The status of the action.
     */
    public LogModel(String emailAddress, String action, String status) {
        this.emailAddress = emailAddress;
        this.action = action;
        this.status = status;
        this.timeStamp = LocalDateTime.now(); // Sets the current timestamp
    }

    /**
     * Constructor to initialize a log entry with the given details, including a specified timestamp.
     *
     * @param logId The unique identifier of the log entry.
     * @param emailAddress The email address of the user.
     * @param action The action performed by the user.
     * @param status The status of the action.
     * @param timestamp The timestamp when the log entry was created.
     */
    public LogModel(int logId, String emailAddress, String action, String status, LocalDateTime timestamp) {
        this.logId = logId;
        this.emailAddress = emailAddress;
        this.action = action;
        this.status = status;
        this.timeStamp = timestamp;
    }

    /**
     * Gets the unique identifier for the log entry.
     *
     * @return The log entry's ID.
     */
    public int getLogId() {
        return logId;
    }

    /**
     * Sets the unique identifier for the log entry.
     *
     * @param logId The log entry's ID.
     */
    public void setLogId(int logId) {
        this.logId = logId;
    }

    /**
     * Gets the email address of the user performing the action.
     *
     * @return The user's email address.
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets the email address of the user performing the action.
     *
     * @param emailAddress The email address to set.
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Gets the action that was performed by the user.
     *
     * @return The action description.
     */
    public String getAction() {
        return action;
    }

    /**
     * Sets the action that was performed by the user.
     *
     * @param action The action description to set.
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * Gets the status of the action.
     *
     * @return The status of the action.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the action.
     *
     * @param status The status of the action to set.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the timestamp when the log entry was created.
     *
     * @return The timestamp of the log entry.
     */
    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    /**
     * Sets the timestamp when the log entry was created.
     *
     * @param timeStamp The timestamp to set.
     */
    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}
