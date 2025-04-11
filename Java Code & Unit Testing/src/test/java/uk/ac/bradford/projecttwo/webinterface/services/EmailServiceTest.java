package uk.ac.bradford.projecttwo.webinterface.services;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    private Gmail gmailMock;
    private EmailService emailService;

    @BeforeEach
    void setUp() throws Exception {
        // Mock Gmail service
        gmailMock = mock(Gmail.class);

        // Mock users().messages().send().execute()
        Gmail.Users users = mock(Gmail.Users.class);
        Gmail.Users.Messages messages = mock(Gmail.Users.Messages.class);
        Gmail.Users.Messages.Send send = mock(Gmail.Users.Messages.Send.class);

        when(gmailMock.users()).thenReturn(users);
        when(users.messages()).thenReturn(messages);
        when(messages.send(eq("me"), any(Message.class))).thenReturn(send);
        when(send.execute()).thenReturn(new Message());

        // Inject mocked Gmail into EmailService
        emailService = new EmailService() {
            @Override
            protected void sendMessage(MimeMessage email) throws IOException, MessagingException {
                // override to prevent real sending
            }
        };
    }

    @Test
    void testSendOtpEmailDoesNotThrow() {
        assertDoesNotThrow(() -> {
            emailService.sendOtpEmail("test@example.com", "123456");
        });
    }

    @Test
    void testCreateEmailSuccess() throws MessagingException {
        MimeMessage msg = emailService.createEmail(
                "to@example.com",
                "from@example.com",
                "Subject Line",
                "This is a test body"
        );
        // Very basic check
        assert msg.getSubject().equals("Subject Line");
    }

    @Test
    void testSendSignupNotificationToAdmin() {
        assertDoesNotThrow(() -> {
            emailService.sendSignupNotificationToAdmin("Test User", "test@example.com");
        });
    }

    @Test
    void testSendApprovalNotification() {
        assertDoesNotThrow(() -> {
            emailService.sendApprovalNotification("test@example.com", "Test User");
        });
    }
}
