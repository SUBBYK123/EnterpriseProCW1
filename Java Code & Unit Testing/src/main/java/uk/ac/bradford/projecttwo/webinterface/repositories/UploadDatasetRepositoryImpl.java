package uk.ac.bradford.projecttwo.webinterface.repositories;

import org.springframework.stereotype.Repository;
import uk.ac.bradford.projecttwo.webinterface.models.UploadDatasetModel;

import java.sql.*;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class UploadDatasetRepositoryImpl implements UploadDatasetRepository{

    private final String JDBC_URL = "jdbc:mysql://localhost:3306/project_two";
    private final String JDBC_USER = "root";
    private final String JDBC_PASSWORD = "Pakistan@1";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    @Override
    public boolean uploadDataset(UploadDatasetModel model) {
        String tableName = model.getDatasetName().replaceAll("[^a-zA-Z0-9_]", "_");
        String createSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (id INT AUTO_INCREMENT PRIMARY KEY, " +
                model.getColumns().stream().map(col -> col + " VARCHAR(255)").collect(Collectors.joining(", ")) + ")";

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(createSQL);

            for (Map<String, Object> row : model.getData()) {
                String insertSQL = "INSERT INTO " + tableName + " (" +
                        String.join(", ", model.getColumns()) + ") VALUES (" +
                        model.getColumns().stream().map(c -> "?").collect(Collectors.joining(", ")) + ")";

                try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                    for (int i = 0; i < model.getColumns().size(); i++) {
                        pstmt.setObject(i + 1, row.get(model.getColumns().get(i)));
                    }
                    pstmt.executeUpdate();
                }
            }

            String insertMetadata = "INSERT INTO dataset_metadata (dataset_name, department, uploaded_by, role, upload_date) " +
                    "VALUES (?, ?, ?, ?, NOW())";
            try (PreparedStatement metaStmt = conn.prepareStatement(insertMetadata)) {
                metaStmt.setString(1, model.getDatasetName());
                metaStmt.setString(2, model.getDepartment());
                metaStmt.setString(3, model.getUploadedBy());
                metaStmt.setString(4, model.getRole());
                metaStmt.executeUpdate();
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
