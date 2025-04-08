package uk.ac.bradford.projecttwo.webinterface.repositories;

import org.springframework.web.multipart.MultipartFile;
import uk.ac.bradford.projecttwo.webinterface.models.UploadDatasetModel;

public interface UploadDatasetRepository {
    boolean uploadDataset(UploadDatasetModel model);
    boolean uploadDatasetStreamed(String datasetName, String department, String uploadedBy, String role, MultipartFile file);
}
