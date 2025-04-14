package uk.ac.bradford.projecttwo.webinterface.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import uk.ac.bradford.projecttwo.webinterface.models.PermissionRequestModel;
import uk.ac.bradford.projecttwo.webinterface.security.Encryptor;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation of the {@link PermissionRequestRepository} interface.
 * Handles database operations related to permission requests for roles, departments, and dataset access.
 */
@Repository
public class PermissionRequestRepositoryImpl implements PermissionRequestRepository {

    @Autowired
    private Encryptor encryptor;

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/project_two";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "Pakistan@1";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    /**
     * Fetches all permission requests from the database.
     */
    @Override
    public List<PermissionRequestModel> getAllRequests() {
        List<PermissionRequestModel> requests = new ArrayList<>();
        String sql = "SELECT * FROM permission_requests";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                PermissionRequestModel request = extractRequestFromResultSet(resultSet);
                requests.add(request);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return requests;
    }

    /**
     * Approves a permission request by updating its status to "APPROVED".
     */
    @Override
    public boolean approveRequest(int requestId) {
        return updateStatus(requestId, "APPROVED");
    }

    /**
     * Denies a permission request by updating its status to "DENIED".
     */
    @Override
    public boolean denyRequest(int requestId) {
        return updateStatus(requestId, "DENIED");
    }

    /**
     * Approves a request and creates a new user entry using the request's details.
     */
    @Override
    public boolean approveRequestAndCreateUser(int requestId) {
        String selectQuery = "SELECT * FROM permission_requests WHERE request_id = ?";
        String insertUserQuery = "INSERT INTO user (first_name, last_name, email_address, password_hash, department) VALUES (?, ?, ?, ?, ?)";
        String updateStatusQuery = "UPDATE permission_requests SET status = 'APPROVED' WHERE request_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertUserQuery);
             PreparedStatement updateStmt = conn.prepareStatement(updateStatusQuery)) {

            selectStmt.setInt(1, requestId);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String hashedPassword = rs.getString("password_hash");
                String department = rs.getString("department");

                insertStmt.setString(1, firstName);
                insertStmt.setString(2, lastName);
                insertStmt.setString(3, email);
                insertStmt.setString(4, hashedPassword);
                insertStmt.setString(5, department);

                if (insertStmt.executeUpdate() > 0) {
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

    /**
     * Retrieves a permission request by ID.
     */
    @Override
    public PermissionRequestModel getRequestById(int requestId) {
        String sql = "SELECT * FROM permission_requests WHERE request_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, requestId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractRequestFromResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns the email address associated with the given request ID.
     */
    @Override
    public String getRequestEmailById(int requestId) {
        String query = "SELECT email FROM permission_requests WHERE request_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, requestId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Saves a new dataset access request entry.
     */
    @Override
    public boolean saveDatasetAccessRequest(String email, String department, String datasetList) {
        String sql = "INSERT INTO permission_requests (email, department, accessible_datasets, requested_role, status) VALUES (?, ?, ?, 'ROLE_USER', 'PENDING')";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);
            statement.setString(2, department);
            statement.setString(3, datasetList);
            return statement.executeUpdate() > 0;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    /**
     * Searches permission requests based on optional filters: email, dataset, department, status.
     */
    @Override
    public List<PermissionRequestModel> searchPermissionRequests(String email, String datasetName, String department, String status) {
        List<PermissionRequestModel> results = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM permission_requests WHERE 1=1");

        List<Object> parameters = new ArrayList<>();

        if (email != null && !email.isBlank()) {
            sql.append(" AND email LIKE ?");
            parameters.add("%" + email + "%");
        }
        if (datasetName != null && !datasetName.isBlank()) {
            sql.append(" AND accessible_datasets LIKE ?");
            parameters.add("%" + datasetName + "%");
        }
        if (department != null && !department.isBlank()) {
            sql.append(" AND department = ?");
            parameters.add(department);
        }
        if (status != null && !status.isBlank()) {
            sql.append(" AND status = ?");
            parameters.add(status.toUpperCase());
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < parameters.size(); i++) {
                stmt.setObject(i + 1, parameters.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PermissionRequestModel req = extractRequestFromResultSet(rs);
                results.add(req);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    /**
     * Helper method to update a request’s status.
     */
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

    /**
     * Helper method to convert a ResultSet row to a {@link PermissionRequestModel}.
     */
    private PermissionRequestModel extractRequestFromResultSet(ResultSet resultSet) throws SQLException {
        PermissionRequestModel request = new PermissionRequestModel();

        request.setRequestId(resultSet.getInt("request_id"));
        request.setFirstName(resultSet.getString("first_name"));
        request.setLastName(resultSet.getString("last_name"));
        request.setEmailAddress(resultSet.getString("email"));
        request.setRequestedRole(resultSet.getString("requested_role"));
        request.setDepartment(resultSet.getString("department"));

        String datasetStr = resultSet.getString("accessible_datasets");
        if (datasetStr != null && !datasetStr.isBlank()) {
            request.setAccessibleDatasets(Arrays.asList(datasetStr.split("\\s*,\\s*")));
        } else {
            request.setAccessibleDatasets(new ArrayList<>());
        }

        request.setStatus(resultSet.getString("status"));
        return request;
    }
}
