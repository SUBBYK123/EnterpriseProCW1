package uk.ac.bradford.projecttwo.webinterface.services;

import uk.ac.bradford.projecttwo.webinterface.models.PermissionRequestModel;

import java.util.List;

public interface PermissionRequestService {
    List<PermissionRequestModel> getAllRequests();
    boolean approveRequest(int requestId);
    boolean denyRequest(int requestId);
}
