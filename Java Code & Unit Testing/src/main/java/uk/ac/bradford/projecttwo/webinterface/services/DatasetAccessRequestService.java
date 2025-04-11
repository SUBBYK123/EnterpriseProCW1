package uk.ac.bradford.projecttwo.webinterface.services;

import uk.ac.bradford.projecttwo.webinterface.models.DatasetAccessRequestModel;

import java.util.List;


public interface DatasetAccessRequestService {
    List<DatasetAccessRequestModel> getAllAccessRequests();
    boolean updateRequestStatus(int requestId, String newStatus);
    boolean hasUserAlreadyRequested(String datasetName, String email);
    void saveAccessRequest(DatasetAccessRequestModel request);
    DatasetAccessRequestModel getRequestByDatasetAndEmail(String datasetName, String email);
    String getRequestEmailById(int requestId);
    boolean isApproved(String datasetName, String userEmail);
    List<DatasetAccessRequestModel> searchDatasetRequests(String email, String datasetName, String department, String status);
    List<DatasetAccessRequestModel> getRequestsByEmail(String email);

}
