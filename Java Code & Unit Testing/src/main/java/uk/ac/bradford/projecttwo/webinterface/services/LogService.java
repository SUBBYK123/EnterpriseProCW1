package uk.ac.bradford.projecttwo.webinterface.services;

import uk.ac.bradford.projecttwo.webinterface.models.LogModel;

import java.util.List;

/**
 * Service interface for logging user actions and retrieving logs.
 * Provides methods to log events and filter or retrieve system logs.
 */
public interface LogService {

    /**
     * Logs a user action in the system.
     *
     * @param emailAddress The email address of the user performing the action.
     * @param action       The action being performed (e.g., "LOGIN", "LOGOUT", "UPLOAD").
     * @param status       The result of the action (e.g., "SUCCESS", "FAILURE").
     */
    void log(String emailAddress, String action, String status);

    /**
     * Retrieves all logs recorded in the system.
     *
     * @return A list of all {@link LogModel} entries.
     */
    List<LogModel> getLogs();

    /**
     * Filters logs based on provided parameters.
     *
     * @param email  The user's email address to filter by (nullable).
     * @param action The action type to filter by (nullable).
     * @param status The status of the action to filter by (nullable).
     * @return A list of {@link LogModel} entries that match the filters.
     */
    List<LogModel> filterLogs(String email, String action, String status);
}
