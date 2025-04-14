package uk.ac.bradford.projecttwo.webinterface.services;

import org.springframework.web.multipart.MultipartFile;
import uk.ac.bradford.projecttwo.webinterface.models.UploadDatasetModel;

import java.util.List;
import java.util.Map;

/**
 * Service interface for handling dataset upload operations,
 * including standard and streamed uploads, content retrieval, and file deletion.
 */
public interface UploadDatasetService {

    /**
     * Processes and uploads a dataset using the data provided in the model.
     *
     * @param model The {@link UploadDatasetModel} containing dataset details and data.
     * @return true if the dataset is successfully uploaded and saved; false otherwise.
     */
    boolean processDatasetUpload(UploadDatasetModel model);

    /**
     * Uploads a dataset using streaming, typically used for handling large files.
     *
     * @param datasetName Name of the dataset.
     * @param department Department to which the dataset belongs.
     * @param uploadedBy Email of the user uploading the dataset.
     * @param role Role of the user uploading the dataset.
     * @param file The dataset file being uploaded.
     * @return true if the dataset is successfully processed and saved; false otherwise.
     */
    boolean uploadDatasetStreamed(String datasetName, String department, String uploadedBy, String role, MultipartFile file);

    /**
     * Retrieves the content of a dataset by its name.
     *
     * @param datasetName The name of the dataset.
     * @return A list of rows where each row is a map of column names to values.
     */
    List<Map<String, Object>> getDatasetContent(String datasetName);

    /**
     * Deletes the physical dataset file from the system.
     *
     * @param datasetName The name of the dataset whose file should be deleted.
     */
    void deleteDatasetFile(String datasetName);
}
