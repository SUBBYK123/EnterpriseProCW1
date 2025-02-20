package uk.ac.bradford.projecttwo.webinterface.repositories;

import org.springframework.stereotype.Repository;
import uk.ac.bradford.projecttwo.webinterface.interfaces.RegistrationInterface;
import uk.ac.bradford.projecttwo.webinterface.models.RegistrationModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RegistrationRepository implements RegistrationInterface {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "Pakistan@1";

    // JDBC Connection Method
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    // ✅ Register User - Fixed SQL Syntax & Resource Management
    @Override
    public boolean registerUser(RegistrationModel registerUser) {
        String sqlQuery = "INSERT INTO user (first_name, last_name, email_address, password_hash, department) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setString(1, registerUser.getFirstName());
            preparedStatement.setString(2, registerUser.getLastName());
            preparedStatement.setString(3, registerUser.getEmailAddress());
            preparedStatement.setString(4, registerUser.getPassword());
            preparedStatement.setString(5, registerUser.getDepartment());

            return preparedStatement.executeUpdate() > 0;  // Returns true if successful

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Find User by Email - Fixed `getUserDetails` Call
    @Override
    public RegistrationModel findUserByEmail(String email) {
        String query = "SELECT * FROM user WHERE email_address = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return getUserDetails(resultSet);  // ✅ Corrected method call
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;  // User not found
    }

    // ✅ Get All Users - Fixed `getUserDetails` Call
    @Override
    public List<RegistrationModel> getAllUsers() {
        List<RegistrationModel> users = new ArrayList<>();
        String sqlQuery = "SELECT * FROM user";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {

            while (resultSet.next()) {
                users.add(getUserDetails(resultSet));  // ✅ Corrected method call
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    // ✅ Fixed `getUserDetails` Method - Now Returns `RegistrationModel`
    private RegistrationModel getUserDetails(ResultSet resultSet) throws SQLException {
        RegistrationModel user = new RegistrationModel();
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setEmailAddress(resultSet.getString("email_address"));
        user.setPassword(resultSet.getString("password_hash"));
        user.setDepartment(resultSet.getString("department"));
        return user;  // ✅ Returns the created object
    }
}
