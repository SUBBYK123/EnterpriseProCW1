package uk.ac.bradford.projecttwo.webinterface.repositories;

import org.springframework.stereotype.Repository;
import uk.ac.bradford.projecttwo.webinterface.models.IndividualAssetModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IndividualAssetRepositoryImpl implements IndividualAssetRepository {

    private final String JDBC_URL = "jdbc:mysql://localhost:3306/project_two";
    private final String JDBC_USER = "root";
    private final String JDBC_PASSWORD = "Pakistan@1";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    @Override
    public boolean saveAsset(IndividualAssetModel asset) {
        String sql = "INSERT INTO individual_assets (dataset_name, name, latitude, longitude, created_by) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, asset.getDatasetName());
            stmt.setString(2, asset.getName());
            stmt.setDouble(3, asset.getLatitude());
            stmt.setDouble(4, asset.getLongitude());
            stmt.setString(5, asset.getCreatedBy());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<IndividualAssetModel> getAssetsByDataset(String datasetName) {

        List<IndividualAssetModel> list = new ArrayList<>();
        String sql = "SELECT * FROM individual_assets WHERE dataset_name = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, datasetName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                IndividualAssetModel model = new IndividualAssetModel();
                model.setId(rs.getInt("id"));
                model.setDatasetName(rs.getString("dataset_name"));
                model.setName(rs.getString("name"));
                model.setLatitude(rs.getDouble("latitude"));
                model.setLongitude(rs.getDouble("longitude"));
                model.setCreatedBy(rs.getString("created_by"));
                model.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                list.add(model);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean updateAsset(IndividualAssetModel asset) {
        String sql = "UPDATE individual_assets SET name = ?, latitude = ?, longitude = ? WHERE id = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, asset.getName());
            stmt.setDouble(2, asset.getLatitude());
            stmt.setDouble(3, asset.getLongitude());
            stmt.setInt(4, asset.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteAsset(int id) {
        String sql = "DELETE FROM individual_assets WHERE id = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
