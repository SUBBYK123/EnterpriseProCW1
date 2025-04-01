package uk.ac.bradford.projecttwo.webinterface.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.bradford.projecttwo.webinterface.models.PermissionRequestModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.PermissionRequestRepository;

import java.util.List;

@Service
public class PermissionRequestServiceImpl implements PermissionRequestService{

    private final PermissionRequestRepository permissionRequestRepository;

    @Autowired
    public PermissionRequestServiceImpl(PermissionRequestRepository permissionRequestRepository) {
        this.permissionRequestRepository = permissionRequestRepository;
    }


    @Override
    public List<PermissionRequestModel> getAllRequests() {
        return permissionRequestRepository.getAllRequests();
    }

    @Override
    public boolean approveRequest(int requestId) {
        return permissionRequestRepository.approveRequest(requestId);
    }

    @Override
    public boolean denyRequest(int requestId) {
        return permissionRequestRepository.denyRequest(requestId);
    }
}
