package uk.ac.bradford.projecttwo.webinterface.repositories;

import org.springframework.web.multipart.MultipartFile;
import uk.ac.bradford.projecttwo.webinterface.models.UploadDatasetModel;

import java.util.List;
import java.util.Map;

/**
 * Repository interface for handling dataset uploads, content retrieval, and deletion.
 */
public interface UploadDatasetRepository {

    /**
     * Uploads a dataset using the given structured model.
     *
     * @param model The dataset model containing metadata and content.
     * @return true if the upload is successful, false otherwise.
     */
    boolean uploadDataset(UploadDatasetModel model);

    /**
     * Uploads a dataset using a streamed file approach (typically for large files).
     *
     * @param datasetName The name of the dataset.
     * @param department The department associated with the dataset.
     * @param uploadedBy The user uploading the dataset.
     * @param role The role of the uploader.
     * @param file The uploaded file (e.g., CSV).
     * @return true if the upload is successful, false otherwise.
     */
    boolean uploadDatasetStreamed(String datasetName, String department, String uploadedBy, String role, MultipartFile file);

    /**
     * Retrieves the content of a dataset as a list of records.
     *
     * @param datasetName The name of the dataset to retrieve.
     * @return A list of maps representing rows in the dataset.
     */
    List<Map<String, Object>> getDatasetContent(String datasetName);

    /**
     * Deletes the stored file corresponding to the given dataset name.
     *
     * @param datasetName The name of the dataset to delete.
     */
    void deleteDatasetFile(String datasetName);
}
