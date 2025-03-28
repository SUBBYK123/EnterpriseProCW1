package uk.ac.bradford.projecttwo.webinterface.services;

import org.junit.jupiter.api.*;
import uk.ac.bradford.projecttwo.webinterface.models.LogModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.LogRepositoryImpl;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LogRepositoryImplTest {

    private LogRepositoryImpl logRepository;
    private Connection sharedConnection;

    @BeforeEach
    void setUp() throws Exception {
        // Ensure the H2 driver is loaded
        Class.forName("org.h2.Driver");

        // Create in-memory database connection
        sharedConnection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", "sa", "");

        // Create the logs table
        Statement stmt = sharedConnection.createStatement();
        stmt.executeUpdate("CREATE TABLE logs (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "email VARCHAR(100), " +
                "action VARCHAR(100), " +
                "status VARCHAR(20), " +
                "timestamp TIMESTAMP)");

        // Inject custom connection for testing
        logRepository = new LogRepositoryImpl() {
            @Override
            protected Connection getConnection() {
                return sharedConnection;
            }
        };
    }

    @AfterEach
    void tearDown() throws Exception {
        if (sharedConnection != null && !sharedConnection.isClosed()) {
            sharedConnection.close();
        }
    }

    @Test
    void testSaveLogAndRetrieve() {
        // Save a test log
        LogModel log = new LogModel("test@example.com", "LOGIN", "SUCCESS");
        logRepository.saveLog(log);

        // Retrieve all logs
        List<LogModel> logs = logRepository.getAllLogs();

        // Assertions
        assertEquals(1, logs.size(), "There should be one log entry in the table.");
        LogModel retrieved = logs.get(0);
        assertEquals("test@example.com", retrieved.getEmailAddress());
        assertEquals("LOGIN", retrieved.getAction());
        assertEquals("SUCCESS", retrieved.getStatus());

        System.out.println("✅ Log retrieved: " + retrieved.getEmailAddress() + " | " + retrieved.getAction() + " | " + retrieved.getStatus());
    }
}
