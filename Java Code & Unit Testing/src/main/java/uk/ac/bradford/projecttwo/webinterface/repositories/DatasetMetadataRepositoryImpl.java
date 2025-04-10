package uk.ac.bradford.projecttwo.webinterface.repositories;

import org.springframework.stereotype.Repository;
import uk.ac.bradford.projecttwo.webinterface.models.DatasetMetadataModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DatasetMetadataRepositoryImpl implements DatasetMetadataRepository {

    private final String JDBC_URL = "jdbc:mysql://localhost:3306/project_two";
    private final String JDBC_USER = "root";
    private final String JDBC_PASSWORD = "Pakistan@1";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    @Override
    public boolean saveMetadata(DatasetMetadataModel dataset) {
        String checkQuery = "SELECT COUNT(*) FROM dataset_metadata WHERE dataset_name = ? AND uploaded_by = ?";
        String insertQuery = "INSERT INTO dataset_metadata (dataset_name, department, uploaded_by, role, upload_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection()) {
            // Check for existing entry
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, dataset.getDatasetName());
                checkStmt.setString(2, dataset.getUploadedBy());

                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    // Duplicate found
                    return false;
                }
            }

            // Insert new entry
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setString(1, dataset.getDatasetName());
                insertStmt.setString(2, dataset.getDepartment());
                insertStmt.setString(3, dataset.getUploadedBy());
                insertStmt.setString(4, dataset.getRole());
                insertStmt.setTimestamp(5, Timestamp.valueOf(dataset.getUploadDate()));

                return insertStmt.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public List<DatasetMetadataModel> getAllMetadata() {
        String query = "SELECT * FROM dataset_metadata";
        List<DatasetMetadataModel> list = new ArrayList<>();

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                DatasetMetadataModel model = mapRowToModel(rs);
                list.add(model);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public DatasetMetadataModel findByName(String datasetName) {
        String query = "SELECT * FROM dataset_metadata WHERE dataset_name = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, datasetName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapRowToModel(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public DatasetMetadataModel findByNameAndUploader(String datasetName, String uploadedBy) {
        String query = "SELECT * FROM dataset_metadata WHERE dataset_name = ? AND uploaded_by = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, datasetName);
            stmt.setString(2, uploadedBy);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                DatasetMetadataModel model = new DatasetMetadataModel();
                model.setDatasetName(rs.getString("dataset_name"));
                model.setDepartment(rs.getString("department"));
                model.setUploadedBy(rs.getString("uploaded_by"));
                model.setRole(rs.getString("role"));
                model.setUploadDate(rs.getTimestamp("upload_date").toLocalDateTime());
                return model;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<DatasetMetadataModel> searchAndFilter(String search, String department, String role) {
        List<DatasetMetadataModel> results = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT * FROM dataset_metadata WHERE 1=1");
        if (search != null && !search.isEmpty()) {
            sql.append(" AND (dataset_name LIKE ? OR uploaded_by LIKE ?)");
        }
        if (department != null && !department.isEmpty()) {
            sql.append(" AND department = ?");
        }
        if (role != null && !role.isEmpty()) {
            sql.append(" AND role = ?");
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int index = 1;
            if (search != null && !search.isEmpty()) {
                stmt.setString(index++, "%" + search + "%");
                stmt.setString(index++, "%" + search + "%");
            }
            if (department != null && !department.isEmpty()) {
                stmt.setString(index++, department);
            }
            if (role != null && !role.isEmpty()) {
                stmt.setString(index++, role);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                DatasetMetadataModel meta = new DatasetMetadataModel();
                meta.setDatasetName(rs.getString("dataset_name"));
                meta.setDepartment(rs.getString("department"));
                meta.setUploadedBy(rs.getString("uploaded_by"));
                meta.setRole(rs.getString("role"));
                meta.setUploadDate(rs.getTimestamp("upload_date").toLocalDateTime());
                results.add(meta);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    private DatasetMetadataModel mapRowToModel(ResultSet rs) throws SQLException {
        DatasetMetadataModel model = new DatasetMetadataModel();
        model.setId(rs.getInt("id"));
        model.setDatasetName(rs.getString("dataset_name"));
        model.setDepartment(rs.getString("department"));
        model.setUploadedBy(rs.getString("uploaded_by"));
        model.setRole(rs.getString("role"));
        model.setUploadDate(rs.getTimestamp("upload_date").toLocalDateTime());
        return model;
    }
}
