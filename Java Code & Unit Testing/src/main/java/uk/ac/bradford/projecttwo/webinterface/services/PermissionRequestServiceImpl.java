package uk.ac.bradford.projecttwo.webinterface.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.bradford.projecttwo.webinterface.models.DatasetAccessRequestModel;
import uk.ac.bradford.projecttwo.webinterface.models.PermissionRequestModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.PermissionRequestRepository;

import java.util.List;

@Service
public class PermissionRequestServiceImpl implements PermissionRequestService{

    private final PermissionRequestRepository permissionRequestRepository;

    private final EmailService emailService;

    @Autowired
    public PermissionRequestServiceImpl(PermissionRequestRepository permissionRequestRepository, EmailService emailService) {
        this.permissionRequestRepository = permissionRequestRepository;
        this.emailService = emailService;
    }


    @Override
    public List<PermissionRequestModel> getAllRequests() {
        return permissionRequestRepository.getAllRequests();
    }

    @Override
    public boolean approveRequest(int requestId) {
        PermissionRequestModel request = permissionRequestRepository.getRequestById(requestId); // You may need to add this method
        if (request == null) return false;

        boolean approved = permissionRequestRepository.approveRequestAndCreateUser(requestId);

        if (approved) {
            try {
                emailService.sendApprovalNotification(request.getEmailAddress(), request.getFirstName() + " " + request.getLastName());
            } catch (Exception e) {
                e.printStackTrace(); // Log this in production
            }
        }

        return approved;
    }

    @Override
    public boolean denyRequest(int requestId) {
        return permissionRequestRepository.denyRequest(requestId);
    }

    @Override
    public String getRequestEmailById(int requestId) {
        return permissionRequestRepository.getRequestEmailById(requestId);
    }

    @Override
    public List<PermissionRequestModel> searchPermissionRequests(String email, String datasetName, String department, String status) {
        return permissionRequestRepository.searchPermissionRequests(email,datasetName,department,status);
    }

}
