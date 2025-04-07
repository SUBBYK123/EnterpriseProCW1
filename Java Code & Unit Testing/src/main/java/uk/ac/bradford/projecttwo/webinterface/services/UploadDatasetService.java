package uk.ac.bradford.projecttwo.webinterface.services;

import uk.ac.bradford.projecttwo.webinterface.models.UploadDatasetModel;

public interface UploadDatasetService {

    boolean processDatasetUpload(UploadDatasetModel model);

}
