package uk.ac.bradford.projecttwo.webinterface.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.bradford.projecttwo.webinterface.models.DatasetMetadataModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.DatasetMetadataRepository;

import java.util.List;

/**
 * Service class responsible for handling dataset metadata operations.
 * Provides methods to store, retrieve, update, search, and delete metadata records.
 */
@Service
public class DatasetMetadataService {

    private final DatasetMetadataRepository repository;

    /**
     * Constructor for dependency injection.
     *
     * @param repository The repository used for dataset metadata operations.
     */
    @Autowired
    public DatasetMetadataService(DatasetMetadataRepository repository) {
        this.repository = repository;
    }

    /**
     * Stores dataset metadata if it doesn't already exist.
     *
     * @param metadata The metadata to store.
     * @return true if successfully stored, false otherwise.
     */
    public boolean storeDatasetMetadata(DatasetMetadataModel metadata) {
        return repository.saveMetadata(metadata);
    }

    /**
     * Fetches all dataset metadata entries from the database.
     *
     * @return A list of {@link DatasetMetadataModel} objects.
     */
    public List<DatasetMetadataModel> fetchAllMetadata() {
        return repository.getAllMetadata();
    }

    /**
     * Retrieves metadata for a specific dataset by its name.
     *
     * @param datasetName The name of the dataset.
     * @return The {@link DatasetMetadataModel} if found, null otherwise.
     */
    public DatasetMetadataModel getMetadataByName(String datasetName) {
        return repository.findByName(datasetName);
    }

    /**
     * Checks if a dataset with the given name and uploader already exists.
     *
     * @param datasetName The dataset name to check.
     * @param uploadedBy  The uploader's email.
     * @return true if a duplicate exists, false otherwise.
     */
    public boolean isDuplicateDataset(String datasetName, String uploadedBy) {
        return repository.findByNameAndUploader(datasetName, uploadedBy) != null;
    }

    /**
     * Searches and filters datasets based on keyword, department, and role.
     *
     * @param search     The keyword to search in dataset name or uploader.
     * @param department The department filter.
     * @param role       The role filter.
     * @return A filtered list of {@link DatasetMetadataModel} objects.
     */
    public List<DatasetMetadataModel> searchAndFilter(String search, String department, String role) {
        return repository.searchAndFilter(search, department, role);
    }

    /**
     * Deletes metadata of a dataset by its name.
     *
     * @param datasetName The name of the dataset to delete.
     */
    public void deleteDatasetByName(String datasetName) {
        repository.deleteByName(datasetName);
    }

    /**
     * Updates metadata for a dataset identified by its original name.
     *
     * @param oldName The original name of the dataset.
     * @param updated The updated metadata model.
     */
    public void updateDatasetMetadata(String oldName, DatasetMetadataModel updated) {
        repository.updateMetadata(oldName, updated);
    }
}
