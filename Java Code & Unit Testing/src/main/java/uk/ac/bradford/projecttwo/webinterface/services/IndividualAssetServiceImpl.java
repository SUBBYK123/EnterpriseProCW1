package uk.ac.bradford.projecttwo.webinterface.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.bradford.projecttwo.webinterface.models.IndividualAssetModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.IndividualAssetRepository;

import java.util.List;

/**
 * Implementation of {@link IndividualAssetService} for managing individual map assets.
 * Handles business logic related to adding, retrieving, updating, and deleting assets.
 */
@Service
public class IndividualAssetServiceImpl implements IndividualAssetService {

    @Autowired
    public IndividualAssetRepository assetRepo;

    /**
     * Adds a new asset to the repository.
     *
     * @param asset The {@link IndividualAssetModel} to be added.
     * @return true if the asset was added successfully, false otherwise.
     */
    @Override
    public boolean addAsset(IndividualAssetModel asset) {
        return assetRepo.saveAsset(asset);
    }

    /**
     * Retrieves all assets associated with a specific dataset.
     *
     * @param datasetName The name of the dataset to fetch assets for.
     * @return A list of assets related to the dataset.
     */
    @Override
    public List<IndividualAssetModel> getAssetsByDataset(String datasetName) {
        return assetRepo.getAssetsByDataset(datasetName);
    }

    /**
     * Updates an existing asset in the repository.
     *
     * @param asset The asset with updated information.
     * @return true if the update was successful, false otherwise.
     */
    @Override
    public boolean updateAsset(IndividualAssetModel asset) {
        return assetRepo.updateAsset(asset);
    }

    /**
     * Deletes an asset by its ID.
     *
     * @param id The unique ID of the asset to delete.
     * @return true if the deletion was successful, false otherwise.
     */
    @Override
    public boolean deleteAsset(int id) {
        return assetRepo.deleteAsset(id);
    }
}
