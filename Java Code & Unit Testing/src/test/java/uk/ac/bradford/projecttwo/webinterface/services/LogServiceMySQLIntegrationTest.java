package uk.ac.bradford.projecttwo.webinterface.services;

import org.junit.jupiter.api.*;
import uk.ac.bradford.projecttwo.webinterface.models.LogModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.LogRepositoryImpl;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for {@link LogServiceImpl} that verify logging functionality
 * with an actual MySQL database using {@link LogRepositoryImpl}.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LogServiceMySQLIntegrationTest {

    private LogServiceImpl logService;

    /**
     * Sets up the log service using a real instance of {@link LogRepositoryImpl}
     * to allow testing against a live MySQL database.
     */
    @BeforeEach
    void setUp() {
        // Use actual repository that connects to MySQL
        LogRepositoryImpl repo = new LogRepositoryImpl();
        logService = new LogServiceImpl(repo);
    }

    /**
     * Inserts a log into the MySQL database using the service.
     * This must run before the verification test.
     */
    @Test
    @Order(1)
    void testInsertLogIntoMySQL() {
        logService.log("integration@example.com", "LOGIN", "SUCCESS");
        System.out.println("✅ Log insertion attempted.");
    }

    /**
     * Verifies that the previously inserted log exists in the MySQL database.
     * Directly queries the database using JDBC.
     */
    @Test
    @Order(2)
    void testLogExistsInMySQL() throws SQLException {
        // Connect directly to MySQL to verify
        String JDBC_URL = "jdbc:mysql://localhost:3306/project_two";
        String JDBC_USER = "root";
        String JDBC_PASSWORD = "Pakistan@1";

        Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM logs WHERE email = ? AND action = ? AND status = ?"
        );

        stmt.setString(1, "integration@example.com");
        stmt.setString(2, "LOGIN");
        stmt.setString(3, "SUCCESS");

        ResultSet rs = stmt.executeQuery();

        assertTrue(rs.next(), "Expected log entry was not found in the database.");
        System.out.println("✅ Log verified in MySQL: " + rs.getString("email"));

        rs.close();
        stmt.close();
        conn.close();
    }
}
