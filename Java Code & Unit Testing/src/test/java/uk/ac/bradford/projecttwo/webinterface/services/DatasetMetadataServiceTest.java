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

/**
 * Unit tests for {@link DatasetMetadataService}.
 * These tests validate the business logic for storing, retrieving, and managing dataset metadata.
 */
class DatasetMetadataServiceTest {

    private DatasetMetadataService service;
    private DatasetMetadataRepository repository;

    /**
     * Initializes mock repository and injects it into the service before each test.
     */
    @BeforeEach
    void setUp() {
        repository = mock(DatasetMetadataRepository.class);
        service = new DatasetMetadataService(repository);
    }

    /**
     * Tests that dataset metadata is stored successfully and returns true.
     */
    @Test
    void testStoreDatasetMetadata_ReturnsTrue() {
        DatasetMetadataModel metadata = new DatasetMetadataModel();
        when(repository.saveMetadata(metadata)).thenReturn(true);

        assertTrue(service.storeDatasetMetadata(metadata));
        verify(repository).saveMetadata(metadata);
    }

    /**
     * Verifies that a list of all dataset metadata is fetched correctly.
     */
    @Test
    void testFetchAllMetadata_ReturnsList() {
        List<DatasetMetadataModel> expected = Arrays.asList(new DatasetMetadataModel(), new DatasetMetadataModel());
        when(repository.getAllMetadata()).thenReturn(expected);

        List<DatasetMetadataModel> actual = service.fetchAllMetadata();
        assertEquals(2, actual.size());
        verify(repository).getAllMetadata();
    }

    /**
     * Confirms that metadata can be retrieved by dataset name.
     */
    @Test
    void testGetMetadataByName_Found() {
        DatasetMetadataModel dataset = new DatasetMetadataModel();
        when(repository.findByName("Test")).thenReturn(dataset);

        DatasetMetadataModel result = service.getMetadataByName("Test");
        assertNotNull(result);
    }

    /**
     * Checks that a duplicate dataset (same name and uploader) returns true.
     */
    @Test
    void testIsDuplicateDataset_ReturnsTrueIfExists() {
        when(repository.findByNameAndUploader("Dataset", "User")).thenReturn(new DatasetMetadataModel());

        assertTrue(service.isDuplicateDataset("Dataset", "User"));
    }

    /**
     * Checks that no duplicate dataset is found when none exists.
     */
    @Test
    void testIsDuplicateDataset_ReturnsFalseIfNotExists() {
        when(repository.findByNameAndUploader("Dataset", "User")).thenReturn(null);

        assertFalse(service.isDuplicateDataset("Dataset", "User"));
    }

    /**
     * Tests that the search and filter operation returns expected dataset metadata results.
     */
    @Test
    void testSearchAndFilter_ReturnsFilteredList() {
        List<DatasetMetadataModel> expected = Collections.singletonList(new DatasetMetadataModel());
        when(repository.searchAndFilter("roads", "Department of Place", "Admin")).thenReturn(expected);

        List<DatasetMetadataModel> results = service.searchAndFilter("roads", "Department of Place", "Admin");
        assertEquals(1, results.size());
    }

    /**
     * Verifies that the repository's delete operation is called with the correct dataset name.
     */
    @Test
    void testDeleteDatasetByName_CallsRepository() {
        service.deleteDatasetByName("dataset123");
        verify(repository).deleteByName("dataset123");
    }

    /**
     * Confirms that dataset metadata is updated by delegating the call to the repository.
     */
    @Test
    void testUpdateDatasetMetadata_CallsRepository() {
        DatasetMetadataModel updated = new DatasetMetadataModel();
        service.updateDatasetMetadata("oldName", updated);
        verify(repository).updateMetadata("oldName", updated);
    }
}
