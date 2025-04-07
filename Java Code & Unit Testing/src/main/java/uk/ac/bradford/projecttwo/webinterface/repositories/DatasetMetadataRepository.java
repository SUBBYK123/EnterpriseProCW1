package uk.ac.bradford.projecttwo.webinterface.repositories;

import uk.ac.bradford.projecttwo.webinterface.models.DatasetMetadataModel;

import java.util.List;

public interface DatasetMetadataRepository {
    boolean saveMetadata(DatasetMetadataModel dataset);
    List<DatasetMetadataModel> getAllMetadata();
    DatasetMetadataModel findByName(String datasetName);
}
