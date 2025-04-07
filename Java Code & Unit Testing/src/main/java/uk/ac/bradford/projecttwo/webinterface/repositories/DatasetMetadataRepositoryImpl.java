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
        String query = "INSERT INTO dataset_metadata (dataset_name, department, uploaded_by, role, upload_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, dataset.getDatasetName());
            stmt.setString(2, dataset.getDepartment());
            stmt.setString(3, dataset.getUploadedBy());
            stmt.setString(4, dataset.getRole());
            stmt.setTimestamp(5, Timestamp.valueOf(dataset.getUploadDate()));

            return stmt.executeUpdate() > 0;

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
