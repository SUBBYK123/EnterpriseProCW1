package uk.ac.bradford.projecttwo.webinterface.repositories;

import org.springframework.stereotype.Repository;
import uk.ac.bradford.projecttwo.webinterface.models.LogModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LogRepositoryImpl implements LogRepository{

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/project_two";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "Pakistan@1";

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }


    @Override
    public void saveLog(LogModel log) {
        String sql = "INSERT INTO logs (email,action,status,timestamp) VALUES (?,?,?,?)";
        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, log.getEmailAddress());
            statement.setString(2, log.getAction());
            statement.setString(3, log.getStatus());
            statement.setTimestamp(4, Timestamp.valueOf(log.getTimeStamp()));
            statement.executeUpdate();

            System.out.println("✅ Log saved to DB: " + log.getEmailAddress() + " | " + log.getAction());

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public List<LogModel> getAllLogs() {
        List<LogModel> logs = new ArrayList<>();
        String sql = "SELECT * FROM logs ORDER BY timestamp DESC";
        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                logs.add(new LogModel(
                        resultSet.getInt("id"),
                        resultSet.getString("email"),
                        resultSet.getString("action"),
                        resultSet.getString("status"),
                        resultSet.getTimestamp("timestamp").toLocalDateTime()
                ));

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return logs;

    }
}
