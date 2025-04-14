package uk.ac.bradford.projecttwo.webinterface.repositories;

import org.springframework.stereotype.Repository;
import uk.ac.bradford.projecttwo.webinterface.models.DatasetMetadataModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of DatasetMetadataRepository for interacting with MySQL database.
 * Provides CRUD operations for dataset metadata such as saving, retrieving,
 * updating, filtering, and deleting.
 */
@Repository
public class DatasetMetadataRepositoryImpl implements DatasetMetadataRepository {

    private final String JDBC_URL = "jdbc:mysql://localhost:3306/project_two";
    private final String JDBC_USER = "root";
    private final String JDBC_PASSWORD = "Pakistan@1";

    /**
     * Establishes a JDBC connection to the database.
     *
     * @return Connection object
     * @throws SQLException if a connection error occurs
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    /**
     * Saves dataset metadata if it doesn't already exist for the same uploader.
     *
     * @param dataset The dataset metadata to save
     * @return true if saved successfully, false if duplicate exists
     */
    @Override
    public boolean saveMetadata(DatasetMetadataModel dataset) {
        String checkQuery = "SELECT COUNT(*) FROM dataset_metadata WHERE dataset_name = ? AND uploaded_by = ?";
        String insertQuery = "INSERT INTO dataset_metadata (dataset_name, department, uploaded_by, role, upload_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection()) {
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, dataset.getDatasetName());
                checkStmt.setString(2, dataset.getUploadedBy());

                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    return false; // Duplicate
                }
            }

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

    /**
     * Retrieves all dataset metadata records.
     *
     * @return List of DatasetMetadataModel
     */
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

    /**
     * Finds dataset metadata by its name.
     *
     * @param datasetName The name of the dataset
     * @return DatasetMetadataModel or null if not found
     */
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

    /**
     * Finds dataset metadata by dataset name and uploader email.
     *
     * @param datasetName The dataset name
     * @param uploadedBy  The email of the uploader
     * @return DatasetMetadataModel or null
     */
    @Override
    public DatasetMetadataModel findByNameAndUploader(String datasetName, String uploadedBy) {
        String query = "SELECT * FROM dataset_metadata WHERE dataset_name = ? AND uploaded_by = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, datasetName);
            stmt.setString(2, uploadedBy);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRowToModel(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Searches datasets by keyword and filters by department and role.
     *
     * @param search     Search keyword
     * @param department Department filter
     * @param role       Role filter
     * @return Filtered list of DatasetMetadataModel
     */
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
                results.add(mapRowToModel(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    /**
     * Updates an existing dataset's metadata.
     *
     * @param oldName The current dataset name
     * @param updated The new metadata to apply
     */
    @Override
    public void updateMetadata(String oldName, DatasetMetadataModel updated) {
        String sql = "UPDATE dataset_metadata SET dataset_name = ?, department = ?, uploaded_by = ?, role = ? WHERE dataset_name = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, updated.getDatasetName());
            stmt.setString(2, updated.getDepartment());
            stmt.setString(3, updated.getUploadedBy());
            stmt.setString(4, updated.getRole());
            stmt.setString(5, oldName);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a dataset's metadata by name.
     *
     * @param datasetName The name of the dataset to delete
     */
    @Override
    public void deleteByName(String datasetName) {
        String sql = "DELETE FROM dataset_metadata WHERE dataset_name = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, datasetName);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method to map a ResultSet row to a DatasetMetadataModel object.
     *
     * @param rs The ResultSet from the SQL query
     * @return DatasetMetadataModel instance
     * @throws SQLException if column access fails
     */
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
