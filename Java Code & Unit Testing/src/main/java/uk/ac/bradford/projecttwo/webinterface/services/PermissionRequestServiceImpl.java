package uk.ac.bradford.projecttwo.webinterface.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.bradford.projecttwo.webinterface.models.DatasetAccessRequestModel;
import uk.ac.bradford.projecttwo.webinterface.models.PermissionRequestModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.PermissionRequestRepository;

import java.util.List;

/**
 * Service implementation for handling user permission requests, such as requesting roles or dataset access.
 */
@Service
public class PermissionRequestServiceImpl implements PermissionRequestService {

    private final PermissionRequestRepository permissionRequestRepository;
    private final EmailService emailService;

    /**
     * Constructs the service with required dependencies.
     *
     * @param permissionRequestRepository Repository for handling permission request persistence.
     * @param emailService Service for sending email notifications.
     */
    @Autowired
    public PermissionRequestServiceImpl(PermissionRequestRepository permissionRequestRepository, EmailService emailService) {
        this.permissionRequestRepository = permissionRequestRepository;
        this.emailService = emailService;
    }

    /**
     * Retrieves all permission requests from the repository.
     *
     * @return A list of all {@link PermissionRequestModel} instances.
     */
    @Override
    public List<PermissionRequestModel> getAllRequests() {
        return permissionRequestRepository.getAllRequests();
    }

    /**
     * Approves a permission request by its ID, creates a user, and sends a notification email.
     *
     * @param requestId The ID of the request to approve.
     * @return true if the request was successfully approved and the user was created, false otherwise.
     */
    @Override
    public boolean approveRequest(int requestId) {
        PermissionRequestModel request = permissionRequestRepository.getRequestById(requestId);
        if (request == null) return false;

        boolean approved = permissionRequestRepository.approveRequestAndCreateUser(requestId);

        if (approved) {
            try {
                emailService.sendApprovalNotification(
                        request.getEmailAddress(),
                        request.getFirstName() + " " + request.getLastName()
                );
            } catch (Exception e) {
                e.printStackTrace(); // You may replace with logging in production
            }
        }

        return approved;
    }

    /**
     * Denies a permission request by its ID.
     *
     * @param requestId The ID of the request to deny.
     * @return true if the request was successfully denied, false otherwise.
     */
    @Override
    public boolean denyRequest(int requestId) {
        return permissionRequestRepository.denyRequest(requestId);
    }

    /**
     * Retrieves the email associated with a specific permission request ID.
     *
     * @param requestId The ID of the request.
     * @return The email address of the requester, or null if not found.
     */
    @Override
    public String getRequestEmailById(int requestId) {
        return permissionRequestRepository.getRequestEmailById(requestId);
    }

    /**
     * Searches permission requests based on provided filter criteria.
     *
     * @param email The email to filter by (optional).
     * @param datasetName The dataset name to filter by (optional).
     * @param department The department to filter by (optional).
     * @param status The request status to filter by (optional).
     * @return A list of matching {@link PermissionRequestModel} instances.
     */
    @Override
    public List<PermissionRequestModel> searchPermissionRequests(String email, String datasetName, String department, String status) {
        return permissionRequestRepository.searchPermissionRequests(email, datasetName, department, status);
    }

}
