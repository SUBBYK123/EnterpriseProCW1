package uk.ac.bradford.projecttwo.webinterface.repositories;

import uk.ac.bradford.projecttwo.webinterface.models.LogModel;

import java.util.List;

/**
 * LogRepository interface defines methods for storing and retrieving
 * log entries related to user actions within the system.
 */
public interface LogRepository {

    /**
     * Persists a log entry into the database.
     *
     * @param log The LogModel object containing log details (email, action, status, timestamp).
     */
    void saveLog(LogModel log);

    /**
     * Retrieves all logs from the system.
     *
     * @return A list of all LogModel entries stored.
     */
    List<LogModel> getAllLogs();

    /**
     * Filters logs based on optional parameters such as email, action type, and status.
     *
     * @param email  The email of the user who triggered the action (can be null).
     * @param action The type of action performed (e.g., LOGIN, REGISTER) (can be null).
     * @param status The outcome of the action (e.g., SUCCESS, FAILURE) (can be null).
     * @return A list of LogModel entries that match the specified filters.
     */
    List<LogModel> filterLogs(String email, String action, String status);
}
