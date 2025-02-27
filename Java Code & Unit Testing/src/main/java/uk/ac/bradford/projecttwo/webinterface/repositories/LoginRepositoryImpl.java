package uk.ac.bradford.projecttwo.webinterface.repositories;

import org.springframework.stereotype.Repository;
import uk.ac.bradford.projecttwo.webinterface.models.LoginModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LoginRepositoryImpl implements LoginRepository {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/project_two";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "Pakistan@1";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    @Override
    public boolean loginUser(LoginModel loginModel) {
        String sqlQuery = "SELECT * FROM user WHERE email_address = ? AND password_hash = ?";
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            preparedStatement.setString(1, loginModel.getEmailAddress());
            preparedStatement.setString(2, loginModel.getPassword());

            try (ResultSet resultSet = preparedStatement.executeQuery(sqlQuery)) {
                return resultSet.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

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
                            resultSet.getString("password_hash")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log properly in production
        }

        return null; // Return null if no user is found
    }


    @Override
    public List<LoginModel> getAllUsers() {

        List<LoginModel> users = new ArrayList<>();
        String sqlQuery = "SELECT * FROM user";

        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                users.add(new LoginModel(
                        resultSet.getString("email_address"),
                        resultSet.getString("password_hash")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
