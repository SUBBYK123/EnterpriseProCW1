package uk.ac.bradford.projecttwo.webinterface.repositories;

import org.springframework.stereotype.Repository;
import uk.ac.bradford.projecttwo.webinterface.models.DatasetAccessRequestModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DatasetAccessRequestRepositoryImpl implements  DatasetAccessRequestRepository{
    private final String JDBC_URL = "jdbc:mysql://localhost:3306/project_two";
    private final String JDBC_USER = "root";
    private final String JDBC_PASSWORD = "Pakistan@1";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

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

    @Override
    public boolean existsByDatasetAndUser(String datasetName, String userEmail) {
        String query = "SELECT COUNT(*) FROM dataset_access_requests WHERE dataset_name = ? AND requested_by = ?";
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

    @Override
    public String getRequestEmailById(int requestId) {
        String sql = "SELECT requested_by FROM dataset_access_requests WHERE request_id = ?";
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
}
