package uk.ac.bradford.projecttwo.webinterface.services;

import uk.ac.bradford.projecttwo.webinterface.models.IndividualAssetModel;

import java.util.List;

/**
 * Service interface for managing individual assets within a dataset.
 * Defines operations for adding, retrieving, updating, and deleting assets.
 */
public interface IndividualAssetService {

    /**
     * Adds a new individual asset to the system.
     *
     * @param asset The asset to be added.
     * @return true if the asset was successfully added, false otherwise.
     */
    boolean addAsset(IndividualAssetModel asset);

    /**
     * Retrieves all assets associated with a specific dataset.
     *
     * @param datasetName The name of the dataset.
     * @return A list of {@link IndividualAssetModel} objects.
     */
    List<IndividualAssetModel> getAssetsByDataset(String datasetName);

    /**
     * Updates the details of an existing asset.
     *
     * @param asset The asset containing updated information.
     * @return true if the asset was successfully updated, false otherwise.
     */
    boolean updateAsset(IndividualAssetModel asset);

    /**
     * Deletes an asset based on its unique identifier.
     *
     * @param id The ID of the asset to be deleted.
     * @return true if the asset was successfully deleted, false otherwise.
     */
    boolean deleteAsset(int id);
}
