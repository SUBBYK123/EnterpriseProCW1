package uk.ac.bradford.projecttwo.webinterface.repositories;

import uk.ac.bradford.projecttwo.webinterface.models.IndividualAssetModel;

import java.util.List;

public interface IndividualAssetRepository {
    boolean saveAsset(IndividualAssetModel asset);
    List<IndividualAssetModel> getAssetsByDataset(String datasetName);
    boolean updateAsset(IndividualAssetModel asset);
    boolean deleteAsset(int id);
}
