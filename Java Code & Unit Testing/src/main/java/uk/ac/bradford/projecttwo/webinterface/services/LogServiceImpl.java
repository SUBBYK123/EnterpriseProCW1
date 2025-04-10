package uk.ac.bradford.projecttwo.webinterface.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.bradford.projecttwo.webinterface.models.LogModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.LogRepository;

import java.util.List;

@Service
public class LogServiceImpl implements LogService{

    private final LogRepository logRepository;

    @Autowired
    public LogServiceImpl(LogRepository logRepository) {
        this.logRepository = logRepository;
    }


    @Override
    public void log(String emailAddress, String action, String status) {
        LogModel log = new LogModel(emailAddress,action,status);
        logRepository.saveLog(log);
    }

    @Override
    public List<LogModel> getLogs() {
        return logRepository.getAllLogs();
    }

    @Override
    public List<LogModel> filterLogs(String email, String action, String status) {
        return logRepository.filterLogs(email, action, status);
    }
}
