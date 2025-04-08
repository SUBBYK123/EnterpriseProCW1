package uk.ac.bradford.projecttwo.webinterface.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.bradford.projecttwo.webinterface.models.UploadDatasetModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.UploadDatasetRepository;

@Service
public class UploadDatasetServiceImpl implements UploadDatasetService{

    @Autowired
    private UploadDatasetRepository uploadDatasetRepository;

    @Override
    public boolean processDatasetUpload(UploadDatasetModel model) {
        return uploadDatasetRepository.uploadDataset(model);
    }

    @Override
    public boolean uploadDatasetStreamed(String datasetName, String department, String uploadedBy, String role, MultipartFile file) {
        return uploadDatasetRepository.uploadDatasetStreamed(datasetName, department, uploadedBy, role, file);
    }

}
