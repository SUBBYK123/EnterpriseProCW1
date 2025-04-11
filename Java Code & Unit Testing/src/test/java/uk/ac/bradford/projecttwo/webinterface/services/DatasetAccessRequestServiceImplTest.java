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

class DatasetAccessRequestServiceImplTest {

    private DatasetAccessRequestRepository repository;
    private EmailService emailService;
    private DatasetAccessRequestServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(DatasetAccessRequestRepository.class);
        emailService = mock(EmailService.class);
        service = new DatasetAccessRequestServiceImpl(repository, emailService);
    }

    @Test
    void testGetAllAccessRequests() {
        when(repository.getAllRequests()).thenReturn(List.of(new DatasetAccessRequestModel()));
        assertEquals(1, service.getAllAccessRequests().size());
        verify(repository).getAllRequests();
    }

    @Test
    void testUpdateRequestStatus() {
        when(repository.updateRequestStatus(1, "APPROVED")).thenReturn(true);
        assertTrue(service.updateRequestStatus(1, "APPROVED"));
        verify(repository).updateRequestStatus(1, "APPROVED");
    }

    @Test
    void testHasUserAlreadyRequested() {
        when(repository.existsByDatasetAndUser("testDataset", "user@test.com")).thenReturn(true);
        assertTrue(service.hasUserAlreadyRequested("testDataset", "user@test.com"));
    }

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

    @Test
    void testGetRequestByDatasetAndEmail() {
        DatasetAccessRequestModel mockRequest = new DatasetAccessRequestModel();
        when(repository.findByDatasetNameAndEmail("ds", "email")).thenReturn(mockRequest);

        assertEquals(mockRequest, service.getRequestByDatasetAndEmail("ds", "email"));
    }

    @Test
    void testGetRequestEmailById() {
        when(repository.getRequestEmailById(5)).thenReturn("user@domain.com");
        assertEquals("user@domain.com", service.getRequestEmailById(5));
    }

    @Test
    void testIsApproved_WhenApproved() {
        DatasetAccessRequestModel request = new DatasetAccessRequestModel();
        request.setStatus("APPROVED");

        when(repository.findByDatasetNameAndEmail("ds", "email")).thenReturn(request);
        assertTrue(service.isApproved("ds", "email"));
    }

    @Test
    void testIsApproved_WhenNotApproved() {
        DatasetAccessRequestModel request = new DatasetAccessRequestModel();
        request.setStatus("PENDING");

        when(repository.findByDatasetNameAndEmail("ds", "email")).thenReturn(request);
        assertFalse(service.isApproved("ds", "email"));
    }

    @Test
    void testSearchDatasetRequests() {
        List<DatasetAccessRequestModel> expected = Collections.singletonList(new DatasetAccessRequestModel());
        when(repository.searchDatasetRequests("e", "d", "dept", "APPROVED")).thenReturn(expected);

        List<DatasetAccessRequestModel> actual = service.searchDatasetRequests("e", "d", "dept", "APPROVED");
        assertEquals(expected, actual);
    }

    @Test
    void testGetRequestsByEmail() {
        when(repository.findRequestsByEmail("email@test.com")).thenReturn(Arrays.asList(new DatasetAccessRequestModel()));
        assertEquals(1, service.getRequestsByEmail("email@test.com").size());
    }
}
