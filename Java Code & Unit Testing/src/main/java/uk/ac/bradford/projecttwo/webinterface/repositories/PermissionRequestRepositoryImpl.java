package uk.ac.bradford.projecttwo.webinterface.repositories;

import org.springframework.stereotype.Repository;
import uk.ac.bradford.projecttwo.webinterface.models.PermissionRequestModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class PermissionRequestRepositoryImpl implements PermissionRequestRepository{

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/project_two";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "Pakistan@1";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }


    @Override
    public List<PermissionRequestModel> getAllRequests() {
        List<PermissionRequestModel> requests = new ArrayList<>();
        String sql = "SELECT * FROM permission_requests";

        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                PermissionRequestModel request = extractRequestFromResultSet(resultSet);
                requests.add(request);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return requests;
    }



    @Override
    public boolean approveRequest(int requestId) {
        return updateStatus(requestId,"APPROVED");

    }

    @Override
    public boolean denyRequest(int requestId) {
        return updateStatus(requestId,"DENIED");
    }

    private boolean updateStatus(int requestId, String newStatus) {
        String sql = "UPDATE permission_requests SET status = ? WHERE request_id = ? AND status = 'PENDING'";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, newStatus);
            statement.setInt(2, requestId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private PermissionRequestModel extractRequestFromResultSet(ResultSet resultSet) throws SQLException {
        PermissionRequestModel request = new PermissionRequestModel();

        request.setRequestId(resultSet.getInt("request_id"));
        request.setRequestId(resultSet.getInt("request_id"));
        request.setFirstName(resultSet.getString("first_name"));
        request.setLastName(resultSet.getString("last_name"));
        request.setEmailAddress(resultSet.getString("email"));
        request.setRequestedRole(resultSet.getString("requested_role"));
        request.setDepartment(resultSet.getString("department"));

        String datasetStr = resultSet.getString("accessible_datasets");
        List<String> datasets = Arrays.asList(datasetStr.split("\\s*,\\s*"));
        request.setAccessibleDatasets(datasets);

        request.setStatus(resultSet.getString("status"));
        return request;

    }
}
