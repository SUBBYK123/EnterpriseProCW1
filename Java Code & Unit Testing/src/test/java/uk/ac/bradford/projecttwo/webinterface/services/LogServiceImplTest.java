package uk.ac.bradford.projecttwo.webinterface.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import uk.ac.bradford.projecttwo.webinterface.models.LogModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.LogRepositoryImpl;

import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link LogServiceImpl} class.
 * Verifies that user activity logs are properly created and passed to the repository.
 */
public class LogServiceImplTest {

    private LogRepositoryImpl logRepository;
    private LogServiceImpl logService;

    /**
     * Sets up a mocked {@link LogRepositoryImpl} and injects it into the service before each test.
     */
    @BeforeEach
    void setUp() {
        logRepository = mock(LogRepositoryImpl.class);
        logService = new LogServiceImpl(logRepository);
    }

    /**
     * Tests that a successful login event is logged and passed to the repository.
     */
    @Test
    void testLogSuccess() {
        logService.log("test@example.com", "LOGIN", "SUCCESS");

        // Verify that the log was passed to the repository once
        verify(logRepository, times(1)).saveLog(any(LogModel.class));
    }

    /**
     * Tests that a failed login event is also logged and passed to the repository.
     */
    @Test
    void testLogFailed() {
        logService.log("test@example.com", "LOGIN", "FAILED");

        // Verify that the log was passed to the repository once
        verify(logRepository, times(1)).saveLog(any(LogModel.class));
    }
}
