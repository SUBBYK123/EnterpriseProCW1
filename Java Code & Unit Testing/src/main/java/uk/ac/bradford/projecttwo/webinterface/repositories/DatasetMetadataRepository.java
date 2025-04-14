package uk.ac.bradford.projecttwo.webinterface.repositories;

import uk.ac.bradford.projecttwo.webinterface.models.DatasetMetadataModel;

import java.util.List;

/**
 * Interface for performing CRUD operations related to dataset metadata.
 * This includes saving, retrieving, searching, updating, and deleting metadata records.
 */
public interface DatasetMetadataRepository {

    /**
     * Saves the metadata of a dataset to the database.
     *
     * @param dataset The dataset metadata to save.
     * @return true if the metadata was successfully saved, false otherwise.
     */
    boolean saveMetadata(DatasetMetadataModel dataset);

    /**
     * Retrieves metadata for all datasets.
     *
     * @return A list of all dataset metadata.
     */
    List<DatasetMetadataModel> getAllMetadata();

    /**
     * Finds dataset metadata by its name.
     *
     * @param datasetName The name of the dataset.
     * @return The corresponding DatasetMetadataModel, or null if not found.
     */
    DatasetMetadataModel findByName(String datasetName);

    /**
     * Finds dataset metadata by dataset name and uploader's email.
     *
     * @param datasetName The name of the dataset.
     * @param uploadedBy The email address of the uploader.
     * @return The matching DatasetMetadataModel, or null if not found.
     */
    DatasetMetadataModel findByNameAndUploader(String datasetName, String uploadedBy);

    /**
     * Searches for datasets by keyword and filters based on department and role.
     *
     * @param search Keyword to search dataset names.
     * @param department Department to filter by.
     * @param role Role to filter by.
     * @return A list of matching dataset metadata.
     */
    List<DatasetMetadataModel> searchAndFilter(String search, String department, String role);

    /**
     * Updates the metadata of a dataset.
     *
     * @param oldName The current name of the dataset to be updated.
     * @param updated The updated metadata information.
     */
    void updateMetadata(String oldName, DatasetMetadataModel updated);

    /**
     * Deletes a dataset and its metadata based on its name.
     *
     * @param datasetName The name of the dataset to delete.
     */
    void deleteByName(String datasetName);
}
