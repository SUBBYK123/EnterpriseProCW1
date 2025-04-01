package uk.ac.bradford.projecttwo.webinterface.repositories;

import org.hibernate.sql.ast.tree.insert.InsertSelectStatement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import uk.ac.bradford.projecttwo.webinterface.models.PermissionRequestModel;
import uk.ac.bradford.projecttwo.webinterface.security.Encryptor;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class PermissionRequestRepositoryImpl implements PermissionRequestRepository{

    @Autowired
    private Encryptor encryptor;

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/project_two";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "Pakistan@1";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }


    @Override
    public List<PermissionRequestModel> getAllRequests() {
        List<PermissionRequestModel> requests = new ArrayList<>();
        String sql = "SELECT * FROM permission_requests";

        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                PermissionRequestModel request = extractRequestFromResultSet(resultSet);
                requests.add(request);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return requests;
    }



    @Override
    public boolean approveRequest(int requestId) {
        return updateStatus(requestId,"APPROVED");

    }

    @Override
    public boolean denyRequest(int requestId) {

        return updateStatus(requestId,"DENIED");
    }

    @Override
    public boolean approveRequestAndCreateUser(int requestId) {
        String selectQuery = "SELECT * FROM permission_requests WHERE request_id = ?";
        String insertUserQuery = "INSERT INTO user (first_name, last_name, email_address, password_hash, department) VALUES (?, ?, ?, ?, ?)";
        String updateStatusQuery = "UPDATE permission_requests SET status = 'APPROVED' WHERE request_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertUserQuery);
             PreparedStatement updateStmt = conn.prepareStatement(updateStatusQuery)) {

            // Step 1: Select user data from permission_requests
            selectStmt.setInt(1, requestId);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                // ✅ Extract correct column names
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email"); // from permission_requests
                String hashedPassword = rs.getString("password_hash");
                String department = rs.getString("department");

                // Step 2: Insert into user table
                insertStmt.setString(1, firstName);
                insertStmt.setString(2, lastName);
                insertStmt.setString(3, email);              // ✅ goes into email_address
                insertStmt.setString(4, hashedPassword);           // ✅ goes into password_hash
                insertStmt.setString(5, department);

                int inserted = insertStmt.executeUpdate();

                // Step 3: Update status if insert successful
                if (inserted > 0) {
                    updateStmt.setInt(1, requestId);
                    updateStmt.executeUpdate();
                    return true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    @Override
    public PermissionRequestModel getRequestById(int requestId) {
        String sql = "SELECT * FROM permission_requests WHERE request_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, requestId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                PermissionRequestModel req = new PermissionRequestModel();
                req.setRequestId(rs.getInt("request_id"));
                req.setFirstName(rs.getString("first_name"));
                req.setLastName(rs.getString("last_name"));
                req.setEmailAddress(rs.getString("email"));
                // other fields if needed
                return req;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private boolean updateStatus(int requestId, String newStatus) {
        String sql = "UPDATE permission_requests SET status = ? WHERE request_id = ? AND status = 'PENDING'";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, newStatus);
            statement.setInt(2, requestId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private PermissionRequestModel extractRequestFromResultSet(ResultSet resultSet) throws SQLException {
        PermissionRequestModel request = new PermissionRequestModel();

        request.setRequestId(resultSet.getInt("request_id"));
        request.setRequestId(resultSet.getInt("request_id"));
        request.setFirstName(resultSet.getString("first_name"));
        request.setLastName(resultSet.getString("last_name"));
        request.setEmailAddress(resultSet.getString("email"));
        request.setRequestedRole(resultSet.getString("requested_role"));
        request.setDepartment(resultSet.getString("department"));

        String datasetStr = resultSet.getString("accessible_datasets");
        List<String> datasets = new ArrayList<>();

        if (datasetStr != null && !datasetStr.isBlank()) {
            datasets = Arrays.asList(datasetStr.split("\\s*,\\s*"));
        }
        request.setAccessibleDatasets(datasets);

        request.setStatus(resultSet.getString("status"));
        return request;

    }
}
