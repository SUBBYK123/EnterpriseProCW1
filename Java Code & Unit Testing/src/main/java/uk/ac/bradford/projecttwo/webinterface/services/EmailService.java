package uk.ac.bradford.projecttwo.webinterface.services;

import com.google.api.services.gmail.Gmail;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Properties;

@Service
public class EmailService {

    private final Gmail gmailService;


    public EmailService() throws GeneralSecurityException, IOException {
        this.gmailService = GmailService.getGmailService();
    }

    public void sendOtpEmail(String toEmail, String otp) throws MessagingException, IOException {
        MimeMessage email = createEmail(toEmail,"mustafakamran491@gmail.com","Password Reset OTP","Your OTP for password reset is: " + otp + "\nThis code is valid for 15 minutes.");
     sendMessage(email);
    }

    private MimeMessage createEmail(String to, String from, String subject, String body) throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props,null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(from));
        email.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
        email.setSubject(subject);
        email.setText(body, StandardCharsets.UTF_8.name());
        return email;
    }


    protected void sendMessage(MimeMessage email) throws IOException, MessagingException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();

        // Encode the email message in Base64 format
        String encodedEmail = Base64.getUrlEncoder().encodeToString(rawMessageBytes);

        // Create the Gmail API Message Objecta
        com.google.api.services.gmail.model.Message message = new com.google.api.services.gmail.model.Message();
        message.setRaw(encodedEmail);

        // Send the message using the Gmail API
        gmailService.users().messages().send("me", message).execute();
    }
}
