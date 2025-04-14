package uk.ac.bradford.projecttwo.webinterface.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.bradford.projecttwo.webinterface.models.UploadDatasetModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.UploadDatasetRepository;

import java.util.List;
import java.util.Map;

/**
 * Implementation of the {@link UploadDatasetService} interface that delegates dataset upload
 * and retrieval operations to the {@link UploadDatasetRepository}.
 */
@Service
public class UploadDatasetServiceImpl implements UploadDatasetService {

    @Autowired
    private UploadDatasetRepository uploadDatasetRepository;

    /**
     * Constructor for injecting the dataset repository.
     *
     * @param repository The repository responsible for dataset persistence.
     */
    public UploadDatasetServiceImpl(UploadDatasetRepository repository) {
        this.uploadDatasetRepository = repository;
    }

    /**
     * Uploads and processes dataset content using the provided model.
     *
     * @param model The dataset model containing columns and rows.
     * @return true if the upload is successful; false otherwise.
     */
    @Override
    public boolean processDatasetUpload(UploadDatasetModel model) {
        return uploadDatasetRepository.uploadDataset(model);
    }

    /**
     * Uploads a dataset using a file stream, usually used for CSV files.
     *
     * @param datasetName Name of the dataset table.
     * @param department Department associated with the dataset.
     * @param uploadedBy User who uploaded the dataset.
     * @param role Role of the uploader.
     * @param file Multipart file representing the dataset.
     * @return true if the streamed upload is successful; false otherwise.
     */
    @Override
    public boolean uploadDatasetStreamed(String datasetName, String department, String uploadedBy, String role, MultipartFile file) {
        return uploadDatasetRepository.uploadDatasetStreamed(datasetName, department, uploadedBy, role, file);
    }

    /**
     * Retrieves all rows from a dataset based on its name.
     *
     * @param datasetName The name of the dataset.
     * @return A list of maps, where each map represents a row (column-value pairs).
     */
    @Override
    public List<Map<String, Object>> getDatasetContent(String datasetName) {
        return uploadDatasetRepository.getDatasetContent(datasetName);
    }

    /**
     * Deletes the dataset file from the storage directory.
     *
     * @param datasetName Name of the dataset to be deleted.
     */
    @Override
    public void deleteDatasetFile(String datasetName) {
        uploadDatasetRepository.deleteDatasetFile(datasetName);
    }
}
