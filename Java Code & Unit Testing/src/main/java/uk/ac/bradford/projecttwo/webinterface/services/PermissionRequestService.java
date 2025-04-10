package uk.ac.bradford.projecttwo.webinterface.services;

import uk.ac.bradford.projecttwo.webinterface.models.DatasetAccessRequestModel;
import uk.ac.bradford.projecttwo.webinterface.models.PermissionRequestModel;

import java.util.List;

public interface PermissionRequestService {
    List<PermissionRequestModel> getAllRequests();
    boolean approveRequest(int requestId);
    boolean denyRequest(int requestId);
    String getRequestEmailById(int requestId);
    List<PermissionRequestModel> searchPermissionRequests(String email, String datasetName, String department, String status);

}
