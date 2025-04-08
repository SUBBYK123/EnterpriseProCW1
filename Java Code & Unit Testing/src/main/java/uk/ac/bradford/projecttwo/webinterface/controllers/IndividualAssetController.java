package uk.ac.bradford.projecttwo.webinterface.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.ac.bradford.projecttwo.webinterface.models.IndividualAssetModel;
import uk.ac.bradford.projecttwo.webinterface.services.IndividualAssetService;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
public class IndividualAssetController {

    @Autowired
    private IndividualAssetService assetService;

    @PostMapping("/add")
    public String addAsset(@RequestBody IndividualAssetModel asset) {
        return assetService.addAsset(asset) ? "✅ Asset Added" : "❌ Failed to Add Asset";
    }

    @GetMapping("/{datasetName}")
    public List<IndividualAssetModel> getAssets(@PathVariable String datasetName) {
        return assetService.getAssetsByDataset(datasetName);
    }

    @PutMapping("/update")
    public String updateAsset(@RequestBody IndividualAssetModel asset) {
        return assetService.updateAsset(asset) ? "✅ Asset Updated" : "❌ Update Failed";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteAsset(@PathVariable int id) {
        return assetService.deleteAsset(id) ? "✅ Asset Deleted" : "❌ Delete Failed";
    }
}
