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

    @Override
    public List<LogModel> filterLogs(String email, String action, String status) {
        List<LogModel> logs = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM logs WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (email != null && !email.isBlank()) {
            sql.append(" AND email LIKE ?");
            params.add("%" + email + "%");
        }
        if (action != null && !action.isBlank()) {
            sql.append(" AND action LIKE ?");
            params.add("%" + action + "%");
        }
        if (status != null && !status.isBlank()) {
            sql.append(" AND status = ?");
            params.add(status);
        }

        sql.append(" ORDER BY timestamp DESC");

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                logs.add(new LogModel(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("action"),
                        rs.getString("status"),
                        rs.getTimestamp("timestamp").toLocalDateTime()
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return logs;
    }
}
