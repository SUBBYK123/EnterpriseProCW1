package uk.ac.bradford.projecttwo.webinterface.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import uk.ac.bradford.projecttwo.webinterface.models.LogModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.LogRepositoryImpl;

import static org.mockito.Mockito.*;

public class LogServiceImplTest {

    private LogRepositoryImpl logRepository;
    private LogServiceImpl logService;

    @BeforeEach
    void setUp() {
        logRepository = mock(LogRepositoryImpl.class);
        logService = new LogServiceImpl(logRepository);
    }

    @Test
    void testLogSuccess() {
        logService.log("test@example.com", "LOGIN", "SUCCESS");

        // Verify that the log was passed to the repository
        verify(logRepository, times(1)).saveLog(any(LogModel.class));
    }

    @Test
    void testLogFailed() {
        logService.log("test@example.com", "LOGIN", "FAILED");

        verify(logRepository, times(1)).saveLog(any(LogModel.class));
    }
}
