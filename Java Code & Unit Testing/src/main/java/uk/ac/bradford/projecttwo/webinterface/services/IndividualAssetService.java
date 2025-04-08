package uk.ac.bradford.projecttwo.webinterface.services;

import uk.ac.bradford.projecttwo.webinterface.models.IndividualAssetModel;

import java.util.List;

public interface IndividualAssetService {
    boolean addAsset(IndividualAssetModel asset);
    List<IndividualAssetModel> getAssetsByDataset(String datasetName);
    boolean updateAsset(IndividualAssetModel asset);
    boolean deleteAsset(int id);
}
