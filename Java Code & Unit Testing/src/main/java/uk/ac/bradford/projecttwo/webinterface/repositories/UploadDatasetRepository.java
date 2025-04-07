package uk.ac.bradford.projecttwo.webinterface.repositories;

import uk.ac.bradford.projecttwo.webinterface.models.UploadDatasetModel;

public interface UploadDatasetRepository {
    boolean uploadDataset(UploadDatasetModel model);
}
