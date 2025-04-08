package uk.ac.bradford.projecttwo.webinterface.services;

import org.springframework.web.multipart.MultipartFile;
import uk.ac.bradford.projecttwo.webinterface.models.UploadDatasetModel;

public interface UploadDatasetService {

    boolean processDatasetUpload(UploadDatasetModel model);
    boolean uploadDatasetStreamed(String datasetName, String department, String uploadedBy, String role, MultipartFile file);

}
