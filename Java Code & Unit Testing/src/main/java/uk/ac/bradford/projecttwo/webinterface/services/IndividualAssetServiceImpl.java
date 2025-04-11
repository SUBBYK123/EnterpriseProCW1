package uk.ac.bradford.projecttwo.webinterface.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.bradford.projecttwo.webinterface.models.IndividualAssetModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.IndividualAssetRepository;

import java.util.List;

@Service
public class IndividualAssetServiceImpl implements IndividualAssetService {

    @Autowired
    public IndividualAssetRepository assetRepo;

    @Override
    public boolean addAsset(IndividualAssetModel asset) {
        return assetRepo.saveAsset(asset);
    }

    @Override
    public List<IndividualAssetModel> getAssetsByDataset(String datasetName) {
        return assetRepo.getAssetsByDataset(datasetName);
    }

    @Override
    public boolean updateAsset(IndividualAssetModel asset) {
        return assetRepo.updateAsset(asset);
    }

    @Override
    public boolean deleteAsset(int id) {
        return assetRepo.deleteAsset(id);
    }
}
