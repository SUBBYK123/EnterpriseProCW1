package uk.ac.bradford.projecttwo.webinterface.repositories;

import org.springframework.web.multipart.MultipartFile;
import uk.ac.bradford.projecttwo.webinterface.models.UploadDatasetModel;

import java.util.List;
import java.util.Map;

public interface UploadDatasetRepository {
    boolean uploadDataset(UploadDatasetModel model);
    boolean uploadDatasetStreamed(String datasetName, String department, String uploadedBy, String role, MultipartFile file);
    List<Map<String, Object>> getDatasetContent(String datasetName);
    void deleteDatasetFile(String datasetName);

}
