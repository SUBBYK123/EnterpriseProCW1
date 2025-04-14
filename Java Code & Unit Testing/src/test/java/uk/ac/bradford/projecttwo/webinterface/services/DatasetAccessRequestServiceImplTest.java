package uk.ac.bradford.projecttwo.webinterface.services;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.bradford.projecttwo.webinterface.models.DatasetAccessRequestModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.DatasetAccessRequestRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link DatasetAccessRequestServiceImpl} class.
 * These tests verify the behavior of dataset access request functionality,
 * including saving requests, checking statuses, sending emails, and retrieving requests.
 */
class DatasetAccessRequestServiceImplTest {

    private DatasetAccessRequestRepository repository;
    private EmailService emailService;
    private DatasetAccessRequestServiceImpl service;

    /**
     * Sets up mocks for repository and email service before each test.
     */
    @BeforeEach
    void setUp() {
        repository = mock(DatasetAccessRequestRepository.class);
        emailService = mock(EmailService.class);
        service = new DatasetAccessRequestServiceImpl(repository, emailService);
    }

    /**
     * Verifies that all access requests are retrieved correctly from the repository.
     */
    @Test
    void testGetAllAccessRequests() {
        when(repository.getAllRequests()).thenReturn(List.of(new DatasetAccessRequestModel()));
        assertEquals(1, service.getAllAccessRequests().size());
        verify(repository).getAllRequests();
    }

    /**
     * Tests whether the status of a request can be updated and returns true.
     */
    @Test
    void testUpdateRequestStatus() {
        when(repository.updateRequestStatus(1, "APPROVED")).thenReturn(true);
        assertTrue(service.updateRequestStatus(1, "APPROVED"));
        verify(repository).updateRequestStatus(1, "APPROVED");
    }

    /**
     * Checks whether the service correctly identifies if a user has already submitted a request.
     */
    @Test
    void testHasUserAlreadyRequested() {
        when(repository.existsByDatasetAndUser("testDataset", "user@test.com")).thenReturn(true);
        assertTrue(service.hasUserAlreadyRequested("testDataset", "user@test.com"));
    }

    /**
     * Tests that a request is saved and two emails are sent (to admin and user) upon submission.
     */
    @Test
    void testSaveAccessRequest_SendsEmails() throws Exception {
        DatasetAccessRequestModel request = new DatasetAccessRequestModel();
        request.setRequestedBy("user@test.com");
        request.setDatasetName("dataset1");

        MimeMessage mockMsg = mock(MimeMessage.class);
        when(emailService.createEmail(any(), any(), any(), any())).thenReturn(mockMsg);

        service.saveAccessRequest(request);

        verify(repository).saveRequest(request);
        verify(emailService, times(2)).sendMessage(mockMsg);
    }

    /**
     * Verifies the correct request is retrieved using dataset name and user email.
     */
    @Test
    void testGetRequestByDatasetAndEmail() {
        DatasetAccessRequestModel mockRequest = new DatasetAccessRequestModel();
        when(repository.findByDatasetNameAndEmail("ds", "email")).thenReturn(mockRequest);

        assertEquals(mockRequest, service.getRequestByDatasetAndEmail("ds", "email"));
    }

    /**
     * Tests that the correct email address is retrieved by request ID.
     */
    @Test
    void testGetRequestEmailById() {
        when(repository.getRequestEmailById(5)).thenReturn("user@domain.com");
        assertEquals("user@domain.com", service.getRequestEmailById(5));
    }

    /**
     * Confirms that `isApproved` returns true when the request is marked as "APPROVED".
     */
    @Test
    void testIsApproved_WhenApproved() {
        DatasetAccessRequestModel request = new DatasetAccessRequestModel();
        request.setStatus("APPROVED");

        when(repository.findByDatasetNameAndEmail("ds", "email")).thenReturn(request);
        assertTrue(service.isApproved("ds", "email"));
    }

    /**
     * Confirms that `isApproved` returns false when the request is not approved.
     */
    @Test
    void testIsApproved_WhenNotApproved() {
        DatasetAccessRequestModel request = new DatasetAccessRequestModel();
        request.setStatus("PENDING");

        when(repository.findByDatasetNameAndEmail("ds", "email")).thenReturn(request);
        assertFalse(service.isApproved("ds", "email"));
    }

    /**
     * Tests that dataset requests can be filtered using search criteria.
     */
    @Test
    void testSearchDatasetRequests() {
        List<DatasetAccessRequestModel> expected = Collections.singletonList(new DatasetAccessRequestModel());
        when(repository.searchDatasetRequests("e", "d", "dept", "APPROVED")).thenReturn(expected);

        List<DatasetAccessRequestModel> actual = service.searchDatasetRequests("e", "d", "dept", "APPROVED");
        assertEquals(expected, actual);
    }

    /**
     * Verifies that all dataset access requests by a given email are retrieved.
     */
    @Test
    void testGetRequestsByEmail() {
        when(repository.findRequestsByEmail("email@test.com")).thenReturn(Arrays.asList(new DatasetAccessRequestModel()));
        assertEquals(1, service.getRequestsByEmail("email@test.com").size());
    }
}
