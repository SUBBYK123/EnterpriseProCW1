package uk.ac.bradford.projecttwo.webinterface.repositories;

import uk.ac.bradford.projecttwo.webinterface.models.DatasetAccessRequestModel;

import java.util.List;

public interface DatasetAccessRequestRepository {
    boolean saveRequest(DatasetAccessRequestModel request);
    List<DatasetAccessRequestModel> getAllRequests();
    boolean updateRequestStatus(int requestId, String status);
    boolean existsByDatasetAndUser(String datasetName, String userEmail);
    DatasetAccessRequestModel findByDatasetNameAndEmail(String datasetName, String email);
    String getRequestEmailById(int requestId);
    List<DatasetAccessRequestModel> searchDatasetRequests(String email, String datasetName, String department, String status);

}
