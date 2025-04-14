package uk.ac.bradford.projecttwo.webinterface.services;

import uk.ac.bradford.projecttwo.webinterface.models.DatasetAccessRequestModel;

import java.util.List;

/**
 * Service interface for managing dataset access requests.
 * Provides methods for saving, retrieving, updating, and searching user requests
 * related to dataset access.
 */
public interface DatasetAccessRequestService {

    /**
     * Retrieves all dataset access requests from the system.
     *
     * @return A list of all {@link DatasetAccessRequestModel} instances.
     */
    List<DatasetAccessRequestModel> getAllAccessRequests();

    /**
     * Updates the status of a dataset access request.
     *
     * @param requestId  The ID of the request to update.
     * @param newStatus  The new status to assign (e.g., "APPROVED", "DENIED").
     * @return true if the update was successful, false otherwise.
     */
    boolean updateRequestStatus(int requestId, String newStatus);

    /**
     * Checks if a user has already submitted a request for a specific dataset.
     *
     * @param datasetName The name of the dataset.
     * @param email       The user's email address.
     * @return true if a request already exists, false otherwise.
     */
    boolean hasUserAlreadyRequested(String datasetName, String email);

    /**
     * Saves a new dataset access request.
     *
     * @param request The {@link DatasetAccessRequestModel} containing the request details.
     */
    void saveAccessRequest(DatasetAccessRequestModel request);

    /**
     * Retrieves a dataset access request based on the dataset name and user email.
     *
     * @param datasetName The name of the dataset.
     * @param email       The user's email address.
     * @return The corresponding {@link DatasetAccessRequestModel}, or null if not found.
     */
    DatasetAccessRequestModel getRequestByDatasetAndEmail(String datasetName, String email);

    /**
     * Retrieves the email address of the user who submitted a request, using the request ID.
     *
     * @param requestId The ID of the dataset access request.
     * @return The user's email address.
     */
    String getRequestEmailById(int requestId);

    /**
     * Checks if a dataset access request is approved for a given user.
     *
     * @param datasetName The name of the dataset.
     * @param userEmail   The user's email address.
     * @return true if access is approved, false otherwise.
     */
    boolean isApproved(String datasetName, String userEmail);

    /**
     * Searches dataset access requests using multiple filter criteria.
     *
     * @param email       Optional email filter.
     * @param datasetName Optional dataset name filter.
     * @param department  Optional department filter.
     * @param status      Optional request status filter.
     * @return A filtered list of matching {@link DatasetAccessRequestModel} entries.
     */
    List<DatasetAccessRequestModel> searchDatasetRequests(String email, String datasetName, String department, String status);

    /**
     * Retrieves all dataset access requests submitted by a specific user.
     *
     * @param email The user's email address.
     * @return A list of {@link DatasetAccessRequestModel} for the user.
     */
    List<DatasetAccessRequestModel> getRequestsByEmail(String email);
}
