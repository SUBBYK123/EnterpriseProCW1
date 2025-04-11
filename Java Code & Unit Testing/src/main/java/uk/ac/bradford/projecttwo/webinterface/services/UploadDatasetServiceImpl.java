package uk.ac.bradford.projecttwo.webinterface.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.bradford.projecttwo.webinterface.models.UploadDatasetModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.UploadDatasetRepository;

import java.util.List;
import java.util.Map;

@Service
public class UploadDatasetServiceImpl implements UploadDatasetService{

    @Autowired
    private UploadDatasetRepository uploadDatasetRepository;

    public UploadDatasetServiceImpl(UploadDatasetRepository repository) {
        this.uploadDatasetRepository = repository;
    }

    @Override
    public boolean processDatasetUpload(UploadDatasetModel model) {
        return uploadDatasetRepository.uploadDataset(model);
    }

    @Override
    public boolean uploadDatasetStreamed(String datasetName, String department, String uploadedBy, String role, MultipartFile file) {
        return uploadDatasetRepository.uploadDatasetStreamed(datasetName, department, uploadedBy, role, file);
    }

    @Override
    public List<Map<String, Object>> getDatasetContent(String datasetName) {
        return uploadDatasetRepository.getDatasetContent(datasetName);
    }

    @Override
    public void deleteDatasetFile(String datasetName) {
        uploadDatasetRepository.deleteDatasetFile(datasetName);
    }


}
