package uk.ac.bradford.projecttwo.webinterface.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.bradford.projecttwo.webinterface.models.UploadDatasetModel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Arrays;
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

    @Override
    public boolean uploadDatasetStreamed(String datasetName, String department, String uploadedBy, String role, MultipartFile file) {
        String tableName = datasetName.replaceAll("[^a-zA-Z0-9_]", "_");

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            // 1. Read the file
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String headerLine = reader.readLine(); // e.g. "name,latitude,longitude"
            if (headerLine == null) return false;

            String[] columns = headerLine.split(",");

            for (int i = 0; i < columns.length; i++) {
                columns[i] = columns[i].replace("\uFEFF", "").trim();
            }

            // 2. Create table if it doesn't exist
            String createSQL = "CREATE TABLE IF NOT EXISTS `" + tableName + "` (id INT AUTO_INCREMENT PRIMARY KEY, " +
                    Arrays.stream(columns)
                            .map(col -> "`" + col.trim() + "` VARCHAR(255)")
                            .collect(Collectors.joining(", ")) +
                    ")";
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createSQL);
            }

            // 3. Prepare insert statement
            String insertSQL = "INSERT INTO `" + tableName + "` (" +
                    Arrays.stream(columns).map(String::trim).collect(Collectors.joining(", ")) +
                    ") VALUES (" +
                    Arrays.stream(columns).map(col -> "?").collect(Collectors.joining(", ")) +
                    ")";
            PreparedStatement pstmt = conn.prepareStatement(insertSQL);

            int batchSize = 0;

            String rowLine;
            while ((rowLine = reader.readLine()) != null) {
                String[] values = rowLine.split(",", -1); // -1 keeps trailing empty values
                for (int i = 0; i < columns.length; i++) {
                    pstmt.setString(i + 1, i < values.length ? values[i].trim() : null);
                }
                pstmt.addBatch();
                batchSize++;

                if (batchSize >= 500) {
                    pstmt.executeBatch();
                    batchSize = 0;
                }
            }

            if (batchSize > 0) {
                pstmt.executeBatch();
            }

            // 4. Insert metadata (skip if already exists)
            String checkMeta = "SELECT COUNT(*) FROM dataset_metadata WHERE dataset_name = ? AND uploaded_by = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkMeta)) {
                checkStmt.setString(1, datasetName);
                checkStmt.setString(2, uploadedBy);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    String insertMeta = "INSERT INTO dataset_metadata (dataset_name, department, uploaded_by, role, upload_date) " +
                            "VALUES (?, ?, ?, ?, NOW())";
                    try (PreparedStatement metaStmt = conn.prepareStatement(insertMeta)) {
                        metaStmt.setString(1, datasetName);
                        metaStmt.setString(2, department);
                        metaStmt.setString(3, uploadedBy);
                        metaStmt.setString(4, role);
                        metaStmt.executeUpdate();
                    }
                }
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
