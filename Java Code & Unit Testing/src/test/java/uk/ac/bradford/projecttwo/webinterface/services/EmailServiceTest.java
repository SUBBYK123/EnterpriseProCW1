package uk.ac.bradford.projecttwo.webinterface.services;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Properties;

import static jakarta.mail.Message.RecipientType.TO;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private Gmail gmailService; // Mocking the Gmail API service

    @InjectMocks
    private EmailService emailService; // Injecting the mock

    @BeforeEach
    void setUp() throws Exception {
        emailService = Mockito.spy(new EmailService()); // Spy on EmailService to test real behavior
    }

    @Test
    void testSendOtpEmail_Success() throws MessagingException, IOException {
        String recipientEmail = "test@example.com";
        String otp = "123456";

        doNothing().when(emailService).sendMessage(any(MimeMessage.class));

        assertDoesNotThrow(() -> emailService.sendOtpEmail(recipientEmail, otp));
    }

    @Test
    void testSendMessage_Success() throws MessagingException, IOException {
        // Create a dummy email message
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress("mustafakamran491@gmail.com"));
        email.addRecipient(TO, new InternetAddress("recipient@example.com"));
        email.setSubject("Test Email");
        email.setText("This is a test email.", StandardCharsets.UTF_8.name());

        // Mock Gmail API response
        Gmail.Users.Messages mockMessages = mock(Gmail.Users.Messages.class);
        Gmail.Users mockUsers = mock(Gmail.Users.class);
        com.google.api.services.gmail.model.Message mockMessage = new com.google.api.services.gmail.model.Message(); // ✅ Create a real instance
        Gmail.Users.Messages.Send mockSend = mock(Gmail.Users.Messages.Send.class); // ✅ Mock Send request

        when(gmailService.users()).thenReturn(mockUsers);
        when(mockUsers.messages()).thenReturn(mockMessages);
        when(mockMessages.send(anyString(), any(com.google.api.services.gmail.model.Message.class))).thenReturn(mockSend); // ✅ Fix applied
        when(mockSend.execute()).thenReturn(mockMessage); // ✅ Ensure execute() returns a Message

        // Test sending email
        assertDoesNotThrow(() -> emailService.sendMessage(email));
        verify(mockMessages, times(1)).send(eq("me"), any(com.google.api.services.gmail.model.Message.class)); // ✅ Ensure API call
    }



}
