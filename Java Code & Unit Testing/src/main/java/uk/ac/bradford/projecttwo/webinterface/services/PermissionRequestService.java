package uk.ac.bradford.projecttwo.webinterface.services;

import uk.ac.bradford.projecttwo.webinterface.models.DatasetAccessRequestModel;
import uk.ac.bradford.projecttwo.webinterface.models.PermissionRequestModel;

import java.util.List;

/**
 * Service interface for handling user permission requests such as role/department changes or dataset access.
 */
public interface PermissionRequestService {

    /**
     * Retrieves all permission requests submitted by users.
     *
     * @return A list of {@link PermissionRequestModel} representing all submitted permission requests.
     */
    List<PermissionRequestModel> getAllRequests();

    /**
     * Approves a specific permission request by its ID.
     *
     * @param requestId The ID of the permission request to approve.
     * @return true if the request was successfully approved, false otherwise.
     */
    boolean approveRequest(int requestId);

    /**
     * Denies a specific permission request by its ID.
     *
     * @param requestId The ID of the permission request to deny.
     * @return true if the request was successfully denied, false otherwise.
     */
    boolean denyRequest(int requestId);

    /**
     * Retrieves the email address associated with a specific permission request ID.
     *
     * @param requestId The ID of the permission request.
     * @return The email address of the user who made the request, or null if not found.
     */
    String getRequestEmailById(int requestId);

    /**
     * Searches for permission requests using provided filters such as email, dataset, department, or status.
     *
     * @param email      Filter by the user's email address (optional).
     * @param datasetName Filter by dataset name (optional).
     * @param department Filter by department name (optional).
     * @param status     Filter by request status (e.g., "PENDING", "APPROVED") (optional).
     * @return A list of {@link PermissionRequestModel} matching the search criteria.
     */
    List<PermissionRequestModel> searchPermissionRequests(String email, String datasetName, String department, String status);

}
