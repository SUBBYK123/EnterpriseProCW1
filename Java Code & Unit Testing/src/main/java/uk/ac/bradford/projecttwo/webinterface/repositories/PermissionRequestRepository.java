package uk.ac.bradford.projecttwo.webinterface.repositories;

import uk.ac.bradford.projecttwo.webinterface.models.PermissionRequestModel;

import java.util.List;

/**
 * Repository interface for handling operations related to user permission requests.
 * These include role upgrades, department changes, and dataset access modifications.
 */
public interface PermissionRequestRepository {

    /**
     * Retrieves all pending and processed permission requests from the database.
     *
     * @return A list of {@link PermissionRequestModel} objects.
     */
    List<PermissionRequestModel> getAllRequests();

    /**
     * Approves a permission request with the given ID by updating its status.
     *
     * @param requestId The ID of the request to approve.
     * @return true if the operation was successful, false otherwise.
     */
    boolean approveRequest(int requestId);

    /**
     * Denies a permission request with the given ID by updating its status.
     *
     * @param requestId The ID of the request to deny.
     * @return true if the operation was successful, false otherwise.
     */
    boolean denyRequest(int requestId);

    /**
     * Approves the permission request and also creates a user record for the requester.
     *
     * @param requestId The ID of the request to process.
     * @return true if both approval and user creation succeed, false otherwise.
     */
    boolean approveRequestAndCreateUser(int requestId);

    /**
     * Retrieves a permission request by its unique ID.
     *
     * @param requestId The request ID to search for.
     * @return A {@link PermissionRequestModel} object if found, null otherwise.
     */
    PermissionRequestModel getRequestById(int requestId);

    /**
     * Retrieves the email address associated with a given request ID.
     *
     * @param requestId The request ID.
     * @return The email address of the requester, or null if not found.
     */
    String getRequestEmailById(int requestId);

    /**
     * Saves a dataset access request for a user to request permission to view certain datasets.
     *
     * @param email        The user's email address.
     * @param department   The user's department.
     * @param datasetList  A comma-separated list of datasets being requested.
     * @return true if the request was saved successfully, false otherwise.
     */
    boolean saveDatasetAccessRequest(String email, String department, String datasetList);

    /**
     * Searches permission requests based on various filters.
     *
     * @param email       The user's email (partial or full).
     * @param datasetName The name of the dataset.
     * @param department  The department name.
     * @param status      The request status (e.g., PENDING, APPROVED).
     * @return A filtered list of {@link PermissionRequestModel} objects.
     */
    List<PermissionRequestModel> searchPermissionRequests(String email, String datasetName, String department, String status);
}
