package uk.ac.bradford.projecttwo.webinterface.repositories;

import uk.ac.bradford.projecttwo.webinterface.models.LogModel;

import java.util.List;

public interface LogRepository {
    void saveLog(LogModel log);
    List<LogModel> getAllLogs();
}
