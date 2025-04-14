package uk.ac.bradford.projecttwo.webinterface.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import uk.ac.bradford.projecttwo.webinterface.models.ResetPasswordModel;
import uk.ac.bradford.projecttwo.webinterface.security.OTPUtil;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ResetPasswordServiceImpl}, verifying OTP generation,
 * validation, and password reset workflows.
 */
public class ResetPasswordServiceImplTest {

    private ResetPasswordServiceImpl service;
    private EmailService emailService;
    private LoginServiceImpl loginService;

    /**
     * Sets up mocked dependencies and injects them into the ResetPasswordServiceImpl instance.
     */
    @BeforeEach
    void setUp() {
        emailService = mock(EmailService.class);
        loginService = mock(LoginServiceImpl.class);
        service = new ResetPasswordServiceImpl();

        // Use spy to inject mocks into private fields
        service = spy(service);
        service.emailService = emailService;
        service.loginService = loginService;
    }

    /**
     * Verifies that OTP is generated, stored, and an email is sent to the user.
     */
    @Test
    void testGenerateAndSendOtpStoresOtpAndSendsEmail() throws Exception {
        String email = "test@example.com";

        service.generateAndSendOtp(email);

        verify(emailService, times(1)).sendOtpEmail(eq(email), anyString());

        // Reflectively access private otpStorage field to confirm storage
        var otpField = ResetPasswordServiceImpl.class.getDeclaredField("otpStorage");
        otpField.setAccessible(true);
        var storage = (Map<String, ResetPasswordModel>) otpField.get(service);

        assertTrue(storage.containsKey(email));
        assertNotNull(storage.get(email).getOtp());
    }

    /**
     * Tests OTP verification success when correct and unexpired OTP is used.
     */
    @Test
    void testVerifyOtpSuccess() throws Exception {
        String email = "user@example.com";
        String otp = "123456";
        ResetPasswordModel token = new ResetPasswordModel(email, otp, LocalDateTime.now().plusMinutes(10));

        var otpField = ResetPasswordServiceImpl.class.getDeclaredField("otpStorage");
        otpField.setAccessible(true);
        Map<String, ResetPasswordModel> map = new HashMap<>();
        map.put(email, token);
        otpField.set(service, map);

        boolean result = service.verifyOtp(email, otp);
        assertTrue(result);
    }

    /**
     * Tests OTP verification failure when incorrect OTP is used.
     */
    @Test
    void testVerifyOtpFailsOnWrongOtp() throws Exception {
        String email = "user@example.com";
        String otp = "123456";
        ResetPasswordModel token = new ResetPasswordModel(email, otp, LocalDateTime.now().plusMinutes(10));

        var otpField = ResetPasswordServiceImpl.class.getDeclaredField("otpStorage");
        otpField.setAccessible(true);
        Map<String, ResetPasswordModel> map = new HashMap<>();
        map.put(email, token);
        otpField.set(service, map);

        boolean result = service.verifyOtp(email, "wrongOtp");
        assertFalse(result);
    }

    /**
     * Tests successful password reset when correct OTP is provided.
     */
    @Test
    void testResetPasswordSuccess() throws Exception {
        String email = "user@example.com";
        String otp = "999999";
        String newPassword = "NewPassword123";
        ResetPasswordModel token = new ResetPasswordModel(email, otp, LocalDateTime.now().plusMinutes(10));

        var otpField = ResetPasswordServiceImpl.class.getDeclaredField("otpStorage");
        otpField.setAccessible(true);
        Map<String, ResetPasswordModel> map = new HashMap<>();
        map.put(email, token);
        otpField.set(service, map);

        boolean result = service.resetPassword(email, otp, newPassword);

        assertTrue(result);
        verify(loginService).updateUserPassword(email, newPassword);
    }

    /**
     * Tests failure of password reset when incorrect OTP is provided.
     */
    @Test
    void testResetPasswordFailsWithInvalidOtp() throws Exception {
        String email = "user@example.com";
        String otp = "validOtp";
        String wrongOtp = "wrongOtp";
        String newPassword = "NewPassword123";
        ResetPasswordModel token = new ResetPasswordModel(email, otp, LocalDateTime.now().plusMinutes(10));

        var otpField = ResetPasswordServiceImpl.class.getDeclaredField("otpStorage");
        otpField.setAccessible(true);
        Map<String, ResetPasswordModel> map = new HashMap<>();
        map.put(email, token);
        otpField.set(service, map);

        boolean result = service.resetPassword(email, wrongOtp, newPassword);

        assertFalse(result);
        verify(loginService, never()).updateUserPassword(anyString(), anyString());
    }
}
