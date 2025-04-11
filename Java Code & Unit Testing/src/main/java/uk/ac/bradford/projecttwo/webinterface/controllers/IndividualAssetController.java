package uk.ac.bradford.projecttwo.webinterface.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.ac.bradford.projecttwo.webinterface.models.IndividualAssetModel;
import uk.ac.bradford.projecttwo.webinterface.services.IndividualAssetService;

import java.util.List;

/**
 * Controller for managing individual assets in the system.
 * Provides RESTful APIs for adding, retrieving, updating, and deleting assets.
 */
@RestController
@RequestMapping("/api/assets")
public class IndividualAssetController {

    // Service for handling individual asset operations
    @Autowired
    private IndividualAssetService assetService;

    /**
     * Adds a new asset to the system.
     *
     * @param asset The asset details to be added, passed as a JSON object.
     * @return A success or failure message based on the result of the operation.
     */
    @PostMapping("/add")
    public String addAsset(@RequestBody IndividualAssetModel asset) {
        return assetService.addAsset(asset) ? "✅ Asset Added" : "❌ Failed to Add Asset";
    }

    /**
     * Retrieves all assets for a specific dataset.
     *
     * @param datasetName The name of the dataset whose assets need to be retrieved.
     * @return A list of individual assets associated with the specified dataset.
     */
    @GetMapping("/{datasetName}")
    public List<IndividualAssetModel> getAssets(@PathVariable String datasetName) {
        return assetService.getAssetsByDataset(datasetName);
    }

    /**
     * Updates an existing asset in the system.
     *
     * @param asset The updated asset details, passed as a JSON object.
     * @return A success or failure message based on the result of the update operation.
     */
    @PutMapping("/update")
    public String updateAsset(@RequestBody IndividualAssetModel asset) {
        return assetService.updateAsset(asset) ? "✅ Asset Updated" : "❌ Update Failed";
    }

    /**
     * Deletes an asset from the system based on its ID.
     *
     * @param id The unique identifier of the asset to be deleted.
     * @return A success or failure message based on the result of the delete operation.
     */
    @DeleteMapping("/delete/{id}")
    public String deleteAsset(@PathVariable int id) {
        return assetService.deleteAsset(id) ? "✅ Asset Deleted" : "❌ Delete Failed";
    }
}
