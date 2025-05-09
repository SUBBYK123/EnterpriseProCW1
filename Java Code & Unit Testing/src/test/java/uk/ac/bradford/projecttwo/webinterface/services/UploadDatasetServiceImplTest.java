package uk.ac.bradford.projecttwo.webinterface.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.bradford.projecttwo.webinterface.models.UploadDatasetModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.UploadDatasetRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link UploadDatasetServiceImpl}, which handles dataset uploading,
 * streaming, content retrieval, and deletion.
 */
class UploadDatasetServiceImplTest {

    private UploadDatasetRepository repository;
    private UploadDatasetServiceImpl service;

    /**
     * Sets up the mock repository and injects it into the service before each test.
     */
    @BeforeEach
    void setUp() {
        repository = mock(UploadDatasetRepository.class);
        service = new UploadDatasetServiceImpl(repository); // ✅ constructor injection
    }

    /**
     * Tests that a dataset is processed and uploaded using the model-based method.
     */
    @Test
    void testProcessDatasetUpload() {
        UploadDatasetModel model = new UploadDatasetModel();
        when(repository.uploadDataset(model)).thenReturn(true);

        boolean result = service.processDatasetUpload(model);

        assertTrue(result);
        verify(repository, times(1)).uploadDataset(model);
    }

    /**
     * Tests the streaming upload functionality using a multipart file.
     */
    @Test
    void testUploadDatasetStreamed() {
        MultipartFile file = mock(MultipartFile.class);
        when(repository.uploadDatasetStreamed("TestData", "Dept", "Admin", "ROLE_ADMIN", file)).thenReturn(true);

        boolean result = service.uploadDatasetStreamed("TestData", "Dept", "Admin", "ROLE_ADMIN", file);

        assertTrue(result);
        verify(repository, times(1)).uploadDatasetStreamed("TestData", "Dept", "Admin", "ROLE_ADMIN", file);
    }

    /**
     * Tests retrieving dataset content from the repository by dataset name.
     */
    @Test
    void testGetDatasetContent() {
        List<Map<String, Object>> mockData = new ArrayList<>();
        Map<String, Object> row = new HashMap<>();
        row.put("name", "Park");
        row.put("latitude", 53.79);
        mockData.add(row);

        when(repository.getDatasetContent("TestDataset")).thenReturn(mockData);

        List<Map<String, Object>> result = service.getDatasetContent("TestDataset");

        assertEquals(1, result.size());
        assertEquals("Park", result.get(0).get("name"));
        verify(repository).getDatasetContent("TestDataset");
    }

    /**
     * Tests that the service correctly delegates dataset file deletion to the repository.
     */
    @Test
    void testDeleteDatasetFile() {
        service.deleteDatasetFile("OldDataset");

        verify(repository, times(1)).deleteDatasetFile("OldDataset");
    }
}
