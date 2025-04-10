package uk.ac.bradford.projecttwo.webinterface.services;

import uk.ac.bradford.projecttwo.webinterface.models.LogModel;

import java.util.List;

public interface LogService {
    void log(String emailAddress, String action, String status);
    List<LogModel> getLogs();
    List<LogModel> filterLogs(String email, String action, String status);

}
