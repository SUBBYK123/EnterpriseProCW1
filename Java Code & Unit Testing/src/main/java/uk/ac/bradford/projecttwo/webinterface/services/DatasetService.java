package uk.ac.bradford.projecttwo.webinterface.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import uk.ac.bradford.projecttwo.webinterface.models.DatasetRequest;

@Service
public class DatasetService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createTableAndSaveData(DatasetRequest datasetRequest) {
        String tableName = datasetRequest.getDatasetName().replaceAll("[^a-zA-Z0-9_]", "_");
        int roleId = datasetRequest.getRoleId();

        // 🚨 Check if dataset already exists in dataset_index 🚨
        String checkDatasetSQL = "SELECT COUNT(*) FROM dataset_index WHERE dataset_name = ? AND role_id = ?";
        Integer count = jdbcTemplate.queryForObject(checkDatasetSQL, Integer.class, tableName, roleId);

        if (count != null && count > 0) {
            throw new RuntimeException("Dataset already uploaded by this role.");
        }

        // 🌟 CREATE TABLE (if not exists)
        List<String> columns = datasetRequest.getColumns();
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (id INT AUTO_INCREMENT PRIMARY KEY, " +
                columns.stream().map(col -> col + " VARCHAR(255)").collect(Collectors.joining(", ")) + ")";
        jdbcTemplate.execute(createTableSQL);

        // 🔹 Insert dataset records
        for (Map<String, Object> row : datasetRequest.getData()) {
            String insertSQL = "INSERT INTO " + tableName + " (" +
                    String.join(", ", columns) + ") VALUES (" +
                    columns.stream().map(col -> "?").collect(Collectors.joining(", ")) + ")";
            jdbcTemplate.update(insertSQL, columns.stream().map(row::get).toArray());
        }

        // ✅ Insert record into dataset_index
        String insertDatasetIndexSQL = "INSERT INTO dataset_index (dataset_name, role_id) VALUES (?, ?)";
        jdbcTemplate.update(insertDatasetIndexSQL, tableName, roleId);
    }
}