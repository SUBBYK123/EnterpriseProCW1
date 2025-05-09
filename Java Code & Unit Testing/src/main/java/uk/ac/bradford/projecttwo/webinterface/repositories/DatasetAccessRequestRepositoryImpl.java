package uk.ac.bradford.projecttwo.webinterface.repositories;

import org.springframework.stereotype.Repository;
import uk.ac.bradford.projecttwo.webinterface.models.DatasetAccessRequestModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the DatasetAccessRequestRepository interface.
 * Handles JDBC operations for dataset access requests such as saving, searching,
 * retrieving, and updating request status.
 */
@Repository
public class DatasetAccessRequestRepositoryImpl implements DatasetAccessRequestRepository {

    private final String JDBC_URL = "jdbc:mysql://localhost:3306/project_two";
    private final String JDBC_USER = "root";
    private final String JDBC_PASSWORD = "Pakistan@1";

    /**
     * Establishes a database connection.
     *
     * @return A valid SQL Connection.
     * @throws SQLException if the connection fails.
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    /**
     * Saves a new dataset access request in the database.
     */
    @Override
    public boolean saveRequest(DatasetAccessRequestModel request) {
        String query = "INSERT INTO dataset_access_requests (dataset_name, requested_by, department, role, request_date, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, request.getDatasetName());
            stmt.setString(2, request.getRequestedBy());
            stmt.setString(3, request.getDepartment());
            stmt.setString(4, request.getRole());
            stmt.setTimestamp(5, Timestamp.valueOf(request.getRequestDate()));
            stmt.setString(6, request.getStatus());

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Returns all access requests from the database.
     */
    @Override
    public List<DatasetAccessRequestModel> getAllRequests() {
        List<DatasetAccessRequestModel> requests = new ArrayList<>();
        String query = "SELECT * FROM dataset_access_requests";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                DatasetAccessRequestModel request = new DatasetAccessRequestModel();
                request.setId(rs.getInt("id"));
                request.setDatasetName(rs.getString("dataset_name"));
                request.setRequestedBy(rs.getString("requested_by"));
                request.setDepartment(rs.getString("department"));
                request.setRole(rs.getString("role"));
                request.setRequestDate(rs.getTimestamp("request_date").toLocalDateTime());
                request.setStatus(rs.getString("status"));
                requests.add(request);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return requests;
    }

    /**
     * Updates the status of a request based on its ID.
     */
    @Override
    public boolean updateRequestStatus(int requestId, String status) {
        String query = "UPDATE dataset_access_requests SET status = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, status);
            stmt.setInt(2, requestId);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Checks if a PENDING request exists for a given user and dataset.
     */
    @Override
    public boolean existsByDatasetAndUser(String datasetName, String userEmail) {
        String query = "SELECT COUNT(*) FROM dataset_access_requests WHERE dataset_name = ? AND requested_by = ? AND status = 'PENDING'";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, datasetName);
            stmt.setString(2, userEmail);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Finds a specific request based on dataset name and requester email.
     */
    @Override
    public DatasetAccessRequestModel findByDatasetNameAndEmail(String datasetName, String email) {
        String query = "SELECT * FROM dataset_access_requests WHERE dataset_name = ? AND requested_by = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, datasetName);
            stmt.setString(2, email);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                DatasetAccessRequestModel request = new DatasetAccessRequestModel();
                request.setId(rs.getInt("id"));
                request.setDatasetName(rs.getString("dataset_name"));
                request.setRequestedBy(rs.getString("requested_by"));
                request.setDepartment(rs.getString("department"));
                request.setStatus(rs.getString("status"));
                return request;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns the email associated with a given request ID.
     */
    @Override
    public String getRequestEmailById(int requestId) {
        String sql = "SELECT requested_by FROM dataset_access_requests WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, requestId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("requested_by");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Filters dataset access requests based on optional parameters.
     */
    @Override
    public List<DatasetAccessRequestModel> searchDatasetRequests(String email, String datasetName, String department, String status) {
        List<DatasetAccessRequestModel> results = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM dataset_access_requests WHERE 1=1");

        List<Object> parameters = new ArrayList<>();

        if (email != null && !email.isBlank()) {
            sql.append(" AND requested_by LIKE ?");
            parameters.add("%" + email + "%");
        }
        if (datasetName != null && !datasetName.isBlank()) {
            sql.append(" AND dataset_name LIKE ?");
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
                DatasetAccessRequestModel request = new DatasetAccessRequestModel();
                request.setId(rs.getInt("id"));
                request.setRequestedBy(rs.getString("requested_by"));
                request.setDepartment(rs.getString("department"));
                request.setDatasetName(rs.getString("dataset_name"));
                request.setRole(rs.getString("role"));
                request.setStatus(rs.getString("status"));
                request.setRequestDate(rs.getTimestamp("request_date").toLocalDateTime());
                results.add(request);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    /**
     * Finds all dataset access requests submitted by a specific user.
     */
    @Override
    public List<DatasetAccessRequestModel> findRequestsByEmail(String email) {
        List<DatasetAccessRequestModel> list = new ArrayList<>();
        String sql = "SELECT * FROM dataset_access_requests WHERE requested_by = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                DatasetAccessRequestModel r = new DatasetAccessRequestModel();
                r.setId(rs.getInt("id"));
                r.setDatasetName(rs.getString("dataset_name"));
                r.setStatus(rs.getString("status"));
                r.setRequestDate(rs.getTimestamp("request_date").toLocalDateTime());
                list.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
