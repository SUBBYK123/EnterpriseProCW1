package uk.ac.bradford.projecttwo.webinterface.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.bradford.projecttwo.webinterface.models.LogModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.LogRepository;

import java.util.List;

/**
 * Implementation of the {@link LogService} interface.
 * Handles logging actions such as saving logs and retrieving/filtering them from the database.
 */
@Service
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;

    /**
     * Constructs the LogServiceImpl with the given LogRepository.
     *
     * @param logRepository The repository used for saving and retrieving logs.
     */
    @Autowired
    public LogServiceImpl(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    /**
     * Saves a new log entry with the given email address, action, and status.
     *
     * @param emailAddress The email address of the user performing the action.
     * @param action       The type of action performed (e.g., "LOGIN", "UPLOAD").
     * @param status       The status of the action (e.g., "SUCCESS", "FAILURE").
     */
    @Override
    public void log(String emailAddress, String action, String status) {
        LogModel log = new LogModel(emailAddress, action, status);
        logRepository.saveLog(log);
    }

    /**
     * Retrieves all logs from the repository.
     *
     * @return A list of all {@link LogModel} entries.
     */
    @Override
    public List<LogModel> getLogs() {
        return logRepository.getAllLogs();
    }

    /**
     * Filters logs by email, action, and status.
     *
     * @param email  The email address to filter by (nullable).
     * @param action The action to filter by (nullable).
     * @param status The status to filter by (nullable).
     * @return A list of filtered {@link LogModel} entries.
     */
    @Override
    public List<LogModel> filterLogs(String email, String action, String status) {
        return logRepository.filterLogs(email, action, status);
    }
}
