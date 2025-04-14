package uk.ac.bradford.projecttwo.webinterface.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.bradford.projecttwo.webinterface.models.IndividualAssetModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.IndividualAssetRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link IndividualAssetServiceImpl}, which handles CRUD operations
 * for individual assets linked to datasets.
 */
class IndividualAssetServiceImplTest {

    private IndividualAssetRepository mockRepo;
    private IndividualAssetServiceImpl assetService;

    /**
     * Sets up a mocked repository and injects it into the service before each test.
     */
    @BeforeEach
    void setUp() {
        mockRepo = mock(IndividualAssetRepository.class);
        assetService = new IndividualAssetServiceImpl();
        assetService.assetRepo = mockRepo; // field injection
    }

    /**
     * Tests successful addition of a new asset using the service.
     */
    @Test
    void testAddAsset_Success() {
        IndividualAssetModel asset = new IndividualAssetModel(1, "dataset1", "Bench", 53.79, -1.75, "admin", null);
        when(mockRepo.saveAsset(asset)).thenReturn(true);

        boolean result = assetService.addAsset(asset);
        assertTrue(result);
        verify(mockRepo).saveAsset(asset);
    }

    /**
     * Tests retrieval of all assets linked to a specific dataset name.
     */
    @Test
    void testGetAssetsByDataset_ReturnsCorrectList() {
        List<IndividualAssetModel> mockAssets = Arrays.asList(
                new IndividualAssetModel(1, "dataset1", "Bench", 53.79, -1.75, "admin", null),
                new IndividualAssetModel(2, "dataset1", "Lamp", 53.78, -1.76, "user", null)
        );

        when(mockRepo.getAssetsByDataset("dataset1")).thenReturn(mockAssets);

        List<IndividualAssetModel> result = assetService.getAssetsByDataset("dataset1");
        assertEquals(2, result.size());
        assertEquals("Bench", result.get(0).getName());
    }

    /**
     * Tests successful updating of an asset's details.
     */
    @Test
    void testUpdateAsset_Success() {
        IndividualAssetModel updatedAsset = new IndividualAssetModel(1, "dataset1", "New Bench", 53.79, -1.75, "admin", null);
        when(mockRepo.updateAsset(updatedAsset)).thenReturn(true);

        boolean result = assetService.updateAsset(updatedAsset);
        assertTrue(result);
        verify(mockRepo).updateAsset(updatedAsset);
    }

    /**
     * Tests successful deletion of an asset by ID.
     */
    @Test
    void testDeleteAsset_Success() {
        int id = 1;
        when(mockRepo.deleteAsset(id)).thenReturn(true);

        boolean result = assetService.deleteAsset(id);
        assertTrue(result);
        verify(mockRepo).deleteAsset(id);
    }
}
