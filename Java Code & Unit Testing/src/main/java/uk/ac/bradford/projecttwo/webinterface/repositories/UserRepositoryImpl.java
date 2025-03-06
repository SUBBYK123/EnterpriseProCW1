package uk.ac.bradford.projecttwo.webinterface.repositories;

import org.springframework.stereotype.Repository;
import uk.ac.bradford.projecttwo.webinterface.models.LoginModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the UserRepository interface.
 * This class handles database operations related to user authentication.
 */
@Repository
public class UserRepositoryImpl implements UserRepository {

    // Database connection details
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/project_two";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "Pakistan@1"; // Consider securing this password using environment variables

    /**
     * Establishes a connection to the database.
     *
     * @return A Connection object.
     * @throws SQLException If an error occurs while establishing the connection.
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    /**
     * Finds a user by their email address in the database.
     *
     * @param email The email address to search for.
     * @return A LoginModel object containing user authentication details if found, otherwise null.
     */
    @Override
    public LoginModel findUserByEmail(String email) {
        String sqlQuery = "SELECT email_address, password_hash FROM user WHERE email_address = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new LoginModel(
                            resultSet.getString("email_address"),
                            resultSet.getString("password_hash") // Ensure proper password security handling
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider using a logger instead of printing the stack trace
        }

        return null; // Return null if no user is found
    }

    /**
     * Retrieves a list of all registered users from the database.
     *
     * @return A List of LoginModel objects representing all users.
     */
    @Override
    public List<LoginModel> getAllUsers() {
        List<LoginModel> users = new ArrayList<>();
        String sqlQuery = "SELECT email_address, password_hash FROM user";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                users.add(new LoginModel(
                        resultSet.getString("email_address"),
                        resultSet.getString("password_hash") // Ensure passwords are securely managed
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Consider logging errors properly
        }

        return users; // Return the list of users instead of null
    }
}
