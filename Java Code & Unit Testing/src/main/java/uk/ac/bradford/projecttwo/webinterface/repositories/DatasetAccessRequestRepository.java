package uk.ac.bradford.projecttwo.webinterface.repositories;

import uk.ac.bradford.projecttwo.webinterface.models.DatasetAccessRequestModel;

import java.util.List;

/**
 * Repository interface for managing dataset access requests.
 * Defines methods for saving, updating, and retrieving access request data from the database.
 */
public interface DatasetAccessRequestRepository {

    /**
     * Saves a new dataset access request to the database.
     *
     * @param request The DatasetAccessRequestModel containing request details.
     * @return true if the request was saved successfully, false otherwise.
     */
    boolean saveRequest(DatasetAccessRequestModel request);

    /**
     * Retrieves all dataset access requests from the database.
     *
     * @return A list of all dataset access requests.
     */
    List<DatasetAccessRequestModel> getAllRequests();

    /**
     * Updates the status of a dataset access request.
     *
     * @param requestId The ID of the request to update.
     * @param status The new status to set (e.g., APPROVED, DENIED).
     * @return true if the update was successful, false otherwise.
     */
    boolean updateRequestStatus(int requestId, String status);

    /**
     * Checks if a request exists for a given dataset and user email.
     *
     * @param datasetName The name of the dataset.
     * @param userEmail The email of the user who may have requested access.
     * @return true if a request exists, false otherwise.
     */
    boolean existsByDatasetAndUser(String datasetName, String userEmail);

    /**
     * Finds a dataset access request by dataset name and user email.
     *
     * @param datasetName The name of the dataset.
     * @param email The email of the user.
     * @return The matching DatasetAccessRequestModel if found, otherwise null.
     */
    DatasetAccessRequestModel findByDatasetNameAndEmail(String datasetName, String email);

    /**
     * Retrieves the email associated with a specific access request ID.
     *
     * @param requestId The ID of the dataset access request.
     * @return The email of the user who made the request.
     */
    String getRequestEmailById(int requestId);

    /**
     * Searches for dataset access requests based on multiple optional criteria.
     *
     * @param email The user email to search for.
     * @param datasetName The dataset name to search for.
     * @param department The department to filter by.
     * @param status The request status to filter by.
     * @return A list of matching DatasetAccessRequestModels.
     */
    List<DatasetAccessRequestModel> searchDatasetRequests(String email, String datasetName, String department, String status);

    /**
     * Retrieves all access requests submitted by a specific user.
     *
     * @param email The email of the user.
     * @return A list of dataset access requests submitted by the user.
     */
    List<DatasetAccessRequestModel> findRequestsByEmail(String email);
}
