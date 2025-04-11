package uk.ac.bradford.projecttwo.webinterface.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.bradford.projecttwo.webinterface.models.DatasetMetadataModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.DatasetMetadataRepository;

import java.util.List;

@Service
public class DatasetMetadataService {

    private final DatasetMetadataRepository repository;

    @Autowired
    public DatasetMetadataService(DatasetMetadataRepository repository) {
        this.repository = repository;
    }

    public boolean storeDatasetMetadata(DatasetMetadataModel metadata) {
        return repository.saveMetadata(metadata);
    }

    public List<DatasetMetadataModel> fetchAllMetadata() {
        return repository.getAllMetadata();
    }

    public DatasetMetadataModel getMetadataByName(String datasetName) {
        return repository.findByName(datasetName);
    }

    public boolean isDuplicateDataset(String datasetName, String uploadedBy) {
        return repository.findByNameAndUploader(datasetName, uploadedBy) != null;
    }

    public List<DatasetMetadataModel> searchAndFilter(String search, String department, String role) {
        return repository.searchAndFilter(search, department, role);
    }


    public void deleteDatasetByName(String datasetName) {
        repository.deleteByName(datasetName);
    }


    public void updateDatasetMetadata(String oldName, DatasetMetadataModel updated) {
        repository.updateMetadata(oldName, updated);
    }


}
