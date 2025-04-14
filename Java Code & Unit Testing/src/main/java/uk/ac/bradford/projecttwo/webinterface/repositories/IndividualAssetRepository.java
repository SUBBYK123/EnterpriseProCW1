package uk.ac.bradford.projecttwo.webinterface.repositories;

import uk.ac.bradford.projecttwo.webinterface.models.IndividualAssetModel;

import java.util.List;

/**
 * Interface for performing CRUD operations on individual map assets.
 * This includes adding, retrieving, updating, and deleting assets associated with a dataset.
 */
public interface IndividualAssetRepository {

    /**
     * Saves a new asset to the database.
     *
     * @param asset The asset to be saved.
     * @return true if the asset is successfully saved, false otherwise.
     */
    boolean saveAsset(IndividualAssetModel asset);

    /**
     * Retrieves all assets associated with a specific dataset.
     *
     * @param datasetName The name of the dataset.
     * @return A list of IndividualAssetModel objects linked to the dataset.
     */
    List<IndividualAssetModel> getAssetsByDataset(String datasetName);

    /**
     * Updates an existing asset in the database.
     *
     * @param asset The asset object containing updated data.
     * @return true if the update was successful, false otherwise.
     */
    boolean updateAsset(IndividualAssetModel asset);

    /**
     * Deletes an asset from the database by its ID.
     *
     * @param id The unique identifier of the asset.
     * @return true if the asset was deleted, false otherwise.
     */
    boolean deleteAsset(int id);
}
