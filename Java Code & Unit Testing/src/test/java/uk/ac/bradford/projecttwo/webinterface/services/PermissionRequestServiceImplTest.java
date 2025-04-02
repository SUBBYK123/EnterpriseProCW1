package uk.ac.bradford.projecttwo.webinterface.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import uk.ac.bradford.projecttwo.webinterface.models.PermissionRequestModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.PermissionRequestRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PermissionRequestServiceImplTest {

    private PermissionRequestRepository permissionRequestRepository;
    private EmailService emailService;
    private PermissionRequestServiceImpl service;

    @BeforeEach
    void setUp() {
        permissionRequestRepository = mock(PermissionRequestRepository.class);
        emailService = mock(EmailService.class);
        service = new PermissionRequestServiceImpl(permissionRequestRepository, emailService);
    }

    @Test
    void testGetAllRequests() {
        PermissionRequestModel request = new PermissionRequestModel();
        when(permissionRequestRepository.getAllRequests()).thenReturn(Arrays.asList(request));

        List<PermissionRequestModel> result = service.getAllRequests();

        assertEquals(1, result.size());
        verify(permissionRequestRepository, times(1)).getAllRequests();
    }

    @Test
    void testApproveRequest_Success() throws Exception {
        int requestId = 1;
        PermissionRequestModel mockRequest = new PermissionRequestModel();
        mockRequest.setEmailAddress("test@example.com");
        mockRequest.setFirstName("Test");
        mockRequest.setLastName("User");

        when(permissionRequestRepository.getRequestById(requestId)).thenReturn(mockRequest);
        when(permissionRequestRepository.approveRequestAndCreateUser(requestId)).thenReturn(true);

        boolean result = service.approveRequest(requestId);

        assertTrue(result);
        verify(emailService, times(1)).sendApprovalNotification(eq("test@example.com"), eq("Test User"));
    }

    @Test
    void testApproveRequest_Failure() throws Exception {
        int requestId = 1;
        when(permissionRequestRepository.getRequestById(requestId)).thenReturn(null);

        boolean result = service.approveRequest(requestId);

        assertFalse(result);
        verify(emailService, never()).sendApprovalNotification(any(), any());
    }

    @Test
    void testDenyRequest() {
        int requestId = 2;
        when(permissionRequestRepository.denyRequest(requestId)).thenReturn(true);

        boolean result = service.denyRequest(requestId);

        assertTrue(result);
        verify(permissionRequestRepository, times(1)).denyRequest(requestId);
    }

    @Test
    void testGetRequestEmailById() {
        int requestId = 3;
        when(permissionRequestRepository.getRequestEmailById(requestId)).thenReturn("admin@council.gov");

        String email = service.getRequestEmailById(requestId);

        assertEquals("admin@council.gov", email);
        verify(permissionRequestRepository, times(1)).getRequestEmailById(requestId);
    }
}
