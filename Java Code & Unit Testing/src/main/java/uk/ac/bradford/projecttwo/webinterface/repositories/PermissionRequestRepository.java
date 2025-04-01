package uk.ac.bradford.projecttwo.webinterface.repositories;

import uk.ac.bradford.projecttwo.webinterface.models.PermissionRequestModel;

import java.util.List;

public interface PermissionRequestRepository {
    List<PermissionRequestModel> getAllRequests();
    boolean approveRequest(int requestId);
    boolean denyRequest(int requestId);
}
