package uk.ac.bradford.projecttwo.webinterface.repositories;

import org.springframework.stereotype.Repository;
import uk.ac.bradford.projecttwo.webinterface.models.RegistrationModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the RegistrationRepository interface.
 * This class handles database operations related to user registration.
 */
@Repository
public class RegistrationRepositoryImpl implements RegistrationRepository {

    // Database connection details
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/project_two";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "Pakistan@1"; // Consider storing passwords securely (e.g., environment
                                                            // variables)

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
     * Registers a new user in the database.
     *
     * @param registerUser The RegistrationModel object containing user details.
     * @return true if the user is successfully registered, false otherwise.
     */
    @Override
    public boolean registerUser(RegistrationModel registerUser) {
        String sqlQuery = "INSERT INTO user (first_name, last_name, email_address, password_hash, department) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            // Setting parameters in the prepared statement
            preparedStatement.setString(1, registerUser.getFirstName());
            preparedStatement.setString(2, registerUser.getLastName());
            preparedStatement.setString(3, registerUser.getEmailAddress());
            preparedStatement.setString(4, registerUser.getPassword()); // Password should be securely hashed
            preparedStatement.setString(5, registerUser.getDepartment());

            return preparedStatement.executeUpdate() > 0; // Returns true if at least one row is affected

        } catch (SQLException e) {
            e.printStackTrace(); // Consider using a logger instead of printing the stack trace
            return false;
        }
    }

    /**
     * Finds a user in the database by their email address.
     *
     * @param email The email address to search for.
     * @return A RegistrationModel object containing user details if found,
     *         otherwise null.
     */
    @Override
    public RegistrationModel findUserByEmail(String email) {
        String query = "SELECT * FROM user WHERE email_address = ?";

        try (Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return getUserDetails(resultSet); // Helper method to extract user details from ResultSet
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Consider logging instead
        }

        return null; // Return null if no user is found
    }

    /**
     * Retrieves all users from the database.
     *
     * @return A List of RegistrationModel objects representing all registered
     *         users.
     */
    @Override
    public List<RegistrationModel> getAllUsers() {
        List<RegistrationModel> users = new ArrayList<>();
        String sqlQuery = "SELECT * FROM user";

        try (Connection connection = getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlQuery)) {

            while (resultSet.next()) {
                users.add(getUserDetails(resultSet)); // Adding each user to the list
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Consider logging instead
        }

        return users;
    }

    /**
     * Extracts user details from a ResultSet and returns a RegistrationModel
     * object.
     *
     * @param resultSet The ResultSet containing user data.
     * @return A RegistrationModel object populated with user details.
     * @throws SQLException If an error occurs while accessing the ResultSet.
     */
    private RegistrationModel getUserDetails(ResultSet resultSet) throws SQLException {
        RegistrationModel user = new RegistrationModel();
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setEmailAddress(resultSet.getString("email_address"));
        user.setPassword(resultSet.getString("password_hash")); // Ensure password is securely handled
        user.setDepartment(resultSet.getString("department"));
        return user;
    }
}
