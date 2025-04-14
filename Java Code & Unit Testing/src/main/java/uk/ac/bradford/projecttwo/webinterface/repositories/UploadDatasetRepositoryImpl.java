package uk.ac.bradford.projecttwo.webinterface.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.bradford.projecttwo.webinterface.models.UploadDatasetModel;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of the UploadDatasetRepository interface.
 * Handles logic for uploading datasets (inline and streamed), creating corresponding
 * tables dynamically, storing metadata, retrieving dataset content, and deleting dataset files.
 */
@Repository
public class UploadDatasetRepositoryImpl implements UploadDatasetRepository {



    private final String JDBC_URL = "jdbc:mysql://localhost:3306/project_two";
    private final String JDBC_USER = "root";
    private final String JDBC_PASSWORD = "Pakistan@1";


    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    /**
     * Uploads a dataset provided via structured UploadDatasetModel.
     * Creates a table dynamically based on column names and inserts data row-wise.
     * Also inserts metadata into the dataset_metadata table.
     *
     * @param model The dataset model containing name, columns, rows, and metadata.
     * @return true if the upload and insertion succeed; false otherwise.
     */
    @Override
    public boolean uploadDataset(UploadDatasetModel model) {
        String tableName = model.getDatasetName().replaceAll("[^a-zA-Z0-9_]", "_");

        String createSQL = "CREATE TABLE IF NOT EXISTS `" + tableName + "` (id INT AUTO_INCREMENT PRIMARY KEY, " +
                model.getColumns().stream()
                        .map(col -> "`" + col + "` VARCHAR(255)")
                        .collect(Collectors.joining(", ")) + ")";

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(createSQL);

            for (Map<String, Object> row : model.getData()) {
                String insertSQL = "INSERT INTO `" + tableName + "` (" +
                        model.getColumns().stream().map(col -> "`" + col + "`").collect(Collectors.joining(", ")) +
                        ") VALUES (" +
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

    /**
     * Uploads a dataset using a MultipartFile stream. Dynamically creates a table,
     * saves the file locally, parses it line by line, and inserts the rows.
     * Also stores dataset metadata conditionally.
     *
     * @param datasetName Name of the dataset (used for table and file naming).
     * @param department Department associated with the upload.
     * @param uploadedBy Email of the uploader.
     * @param role Role of the uploader.
     * @param file Multipart file uploaded by the user (CSV).
     * @return true if upload and processing are successful; false otherwise.
     */
    @Override
    public boolean uploadDatasetStreamed(String datasetName, String department, String uploadedBy, String role, MultipartFile file) {
        String normalisedName = datasetName.trim().replaceAll("[^a-zA-Z0-9_]", "_").toLowerCase();

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            // Save CSV file locally
            File outputDir = new File("src/main/resources/datasets/download");
            if (!outputDir.exists()) outputDir.mkdirs();

            File outputFile = new File(outputDir, datasetName + ".csv");
            try (InputStream in = file.getInputStream(); OutputStream out = new FileOutputStream(outputFile)) {
                in.transferTo(out);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(outputFile)));
            String headerLine = reader.readLine();
            if (headerLine == null) return false;

            String[] columns = headerLine.split(",");
            for (int i = 0; i < columns.length; i++) {
                columns[i] = columns[i].replace("\uFEFF", "").trim();
            }

            String createSQL = "CREATE TABLE IF NOT EXISTS `" + normalisedName + "` (id INT AUTO_INCREMENT PRIMARY KEY, " +
                    Arrays.stream(columns).map(col -> "`" + col + "` VARCHAR(255)").collect(Collectors.joining(", ")) + ")";
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createSQL);
            }

            String insertSQL = "INSERT INTO `" + normalisedName + "` (" +
                    Arrays.stream(columns).map(String::trim).collect(Collectors.joining(", ")) +
                    ") VALUES (" + Arrays.stream(columns).map(col -> "?").collect(Collectors.joining(", ")) + ")";
            PreparedStatement pstmt = conn.prepareStatement(insertSQL);

            int batchSize = 0;
            String rowLine;
            while ((rowLine = reader.readLine()) != null) {
                String[] values = rowLine.split(",", -1);
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

            // Insert metadata if it doesn't already exist
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
        }
        return false;
    }

    /**
     * Fetches the dataset content from the dynamically created table using the dataset name.
     *
     * @param datasetName The name of the dataset/table.
     * @return A list of maps representing each row in the dataset.
     */
    @Override
    public List<Map<String, Object>> getDatasetContent(String datasetName) {
        List<Map<String, Object>> rows = new ArrayList<>();
        String query = "SELECT * FROM `" + datasetName + "`";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(meta.getColumnName(i), rs.getObject(i));
                }
                rows.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rows;
    }

    /**
     * Deletes the stored CSV file associated with the given dataset name.
     *
     * @param datasetName The name of the dataset to delete.
     */
    @Override
    public void deleteDatasetFile(String datasetName) {
        File file = new File("src/main/resources/datasets/download/" + datasetName + ".csv");
        if (file.exists()) {
            file.delete();
        }
    }
}
