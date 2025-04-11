package uk.ac.bradford.projecttwo.webinterface.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import uk.ac.bradford.projecttwo.webinterface.models.DatasetMetadataModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.DatasetMetadataRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DatasetMetadataServiceTest {

    private DatasetMetadataService service;
    private DatasetMetadataRepository repository;

    @BeforeEach
    void setUp() {
        repository = mock(DatasetMetadataRepository.class);
        service = new DatasetMetadataService(repository);
    }

    @Test
    void testStoreDatasetMetadata_ReturnsTrue() {
        DatasetMetadataModel metadata = new DatasetMetadataModel();
        when(repository.saveMetadata(metadata)).thenReturn(true);

        assertTrue(service.storeDatasetMetadata(metadata));
        verify(repository).saveMetadata(metadata);
    }

    @Test
    void testFetchAllMetadata_ReturnsList() {
        List<DatasetMetadataModel> expected = Arrays.asList(new DatasetMetadataModel(), new DatasetMetadataModel());
        when(repository.getAllMetadata()).thenReturn(expected);

        List<DatasetMetadataModel> actual = service.fetchAllMetadata();
        assertEquals(2, actual.size());
        verify(repository).getAllMetadata();
    }

    @Test
    void testGetMetadataByName_Found() {
        DatasetMetadataModel dataset = new DatasetMetadataModel();
        when(repository.findByName("Test")).thenReturn(dataset);

        DatasetMetadataModel result = service.getMetadataByName("Test");
        assertNotNull(result);
    }

    @Test
    void testIsDuplicateDataset_ReturnsTrueIfExists() {
        when(repository.findByNameAndUploader("Dataset", "User")).thenReturn(new DatasetMetadataModel());

        assertTrue(service.isDuplicateDataset("Dataset", "User"));
    }

    @Test
    void testIsDuplicateDataset_ReturnsFalseIfNotExists() {
        when(repository.findByNameAndUploader("Dataset", "User")).thenReturn(null);

        assertFalse(service.isDuplicateDataset("Dataset", "User"));
    }

    @Test
    void testSearchAndFilter_ReturnsFilteredList() {
        List<DatasetMetadataModel> expected = Collections.singletonList(new DatasetMetadataModel());
        when(repository.searchAndFilter("roads", "Department of Place", "Admin")).thenReturn(expected);

        List<DatasetMetadataModel> results = service.searchAndFilter("roads", "Department of Place", "Admin");
        assertEquals(1, results.size());
    }

    @Test
    void testDeleteDatasetByName_CallsRepository() {
        service.deleteDatasetByName("dataset123");
        verify(repository).deleteByName("dataset123");
    }

    @Test
    void testUpdateDatasetMetadata_CallsRepository() {
        DatasetMetadataModel updated = new DatasetMetadataModel();
        service.updateDatasetMetadata("oldName", updated);
        verify(repository).updateMetadata("oldName", updated);
    }
}
