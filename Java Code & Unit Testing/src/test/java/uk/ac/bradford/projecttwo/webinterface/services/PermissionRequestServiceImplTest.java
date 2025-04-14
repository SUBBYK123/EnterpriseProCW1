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

/**
 * Unit tests for {@link PermissionRequestServiceImpl}, which handles logic
 * related to admin approval or denial of permission/role assignment requests.
 */
class PermissionRequestServiceImplTest {

    private PermissionRequestRepository permissionRequestRepository;
    private EmailService emailService;
    private PermissionRequestServiceImpl service;

    /**
     * Initializes mocks and injects dependencies into the service before each test.
     */
    @BeforeEach
    void setUp() {
        permissionRequestRepository = mock(PermissionRequestRepository.class);
        emailService = mock(EmailService.class);
        service = new PermissionRequestServiceImpl(permissionRequestRepository, emailService);
    }

    /**
     * Tests that all permission requests are fetched correctly from the repository.
     */
    @Test
    void testGetAllRequests() {
        PermissionRequestModel request = new PermissionRequestModel();
        when(permissionRequestRepository.getAllRequests()).thenReturn(Arrays.asList(request));

        List<PermissionRequestModel> result = service.getAllRequests();

        assertEquals(1, result.size());
        verify(permissionRequestRepository, times(1)).getAllRequests();
    }

    /**
     * Tests the approval of a permission request, including user creation and notification email.
     */
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

    /**
     * Tests behavior when approval fails due to request not being found.
     */
    @Test
    void testApproveRequest_Failure() throws Exception {
        int requestId = 1;
        when(permissionRequestRepository.getRequestById(requestId)).thenReturn(null);

        boolean result = service.approveRequest(requestId);

        assertFalse(result);
        verify(emailService, never()).sendApprovalNotification(any(), any());
    }

    /**
     * Tests the denial of a permission request.
     */
    @Test
    void testDenyRequest() {
        int requestId = 2;
        when(permissionRequestRepository.denyRequest(requestId)).thenReturn(true);

        boolean result = service.denyRequest(requestId);

        assertTrue(result);
        verify(permissionRequestRepository, times(1)).denyRequest(requestId);
    }

    /**
     * Verifies that the correct email address is retrieved by permission request ID.
     */
    @Test
    void testGetRequestEmailById() {
        int requestId = 3;
        when(permissionRequestRepository.getRequestEmailById(requestId)).thenReturn("admin@council.gov");

        String email = service.getRequestEmailById(requestId);

        assertEquals("admin@council.gov", email);
        verify(permissionRequestRepository, times(1)).getRequestEmailById(requestId);
    }
}
