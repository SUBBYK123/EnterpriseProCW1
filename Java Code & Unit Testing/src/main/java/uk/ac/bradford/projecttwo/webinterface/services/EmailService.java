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

/**
 * Service class for sending emails using Gmail API.
 */
@Service
public class EmailService {

    private final Gmail gmailService;

    /**
     * Constructor initializes the Gmail service by obtaining an authorized instance.
     * @throws GeneralSecurityException If a security-related exception occurs.
     * @throws IOException If an I/O error occurs while initializing the Gmail service.
     */
    public EmailService() throws GeneralSecurityException, IOException {
        this.gmailService = GmailService.getGmailService();
    }

    /**
     * Sends an OTP email to the specified recipient.
     * @param toEmail The recipient's email address.
     * @param otp The OTP code to be sent.
     * @throws MessagingException If an error occurs while creating the email message.
     * @throws IOException If an error occurs while sending the message.
     */
    public void sendOtpEmail(String toEmail, String otp) throws MessagingException, IOException {
        MimeMessage email = createEmail(toEmail, "mustafakamran491@gmail.com", "Password Reset OTP",
                "Your OTP for password reset is: " + otp + "\nThis code is valid for 15 minutes.");
        sendMessage(email);
    }

    /**
     * Creates an email message.
     * @param to Recipient email address.
     * @param from Sender email address.
     * @param subject Subject of the email.
     * @param body Body content of the email.
     * @return A MimeMessage object representing the email.
     * @throws MessagingException If an error occurs while creating the message.
     */
    public MimeMessage createEmail(String to, String from, String subject, String body) throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(from));
        email.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);
        email.setText(body, StandardCharsets.UTF_8.name());
        return email;
    }

    /**
     * Sends an email message using the Gmail API.
     * @param email The MimeMessage object containing the email details.
     * @throws IOException If an error occurs while encoding or sending the message.
     * @throws MessagingException If an error occurs while processing the message.
     */
    protected void sendMessage(MimeMessage email) throws IOException, MessagingException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();

        // Encode the email message in Base64 format
        String encodedEmail = Base64.getUrlEncoder().encodeToString(rawMessageBytes);

        // Create the Gmail API Message Object
        com.google.api.services.gmail.model.Message message = new com.google.api.services.gmail.model.Message();
        message.setRaw(encodedEmail);

        // Send the message using the Gmail API
        gmailService.users().messages().send("me", message).execute();
    }

    public void sendSignupNotificationToAdmin(String fullName, String userEmail) throws MessagingException, IOException {
        String subject = "New User Signup: Action Required!";
        String body = "A new user has registered : \n\n"
                    + "Name: " + fullName + "\n"
                    + "Email: " + userEmail + "\n\n"
                    + "Please review and approve their request in the Permissions page: \n"
                    + "http://localhost:8080/permissions";

                                            //Add admin email
        MimeMessage email = createEmail(userEmail,"mustafakamran491@gmail.com",subject,body);
        sendMessage(email);
    }

    /**
     * Sends a notification email to the user once their registration is approved.
     * @param toEmail The recipient's email address.
     * @param fullName The user's full name.
     */
    public void sendApprovalNotification(String toEmail, String fullName) throws Exception {
        String subject = "Your Account Has Been Approved – Bradford Council Web System";
        String body = "Dear " + fullName + ",\n\n"
                + "Your account registration has been approved by the system administrator.\n\n"
                + "You can now log in to the system using the credentials you provided during signup.\n"
                + "Access the system here: http://localhost:8080/login\n\n"
                + "If you experience any issues, please contact support.\n\n"
                + "Best regards,\n"
                + "City of Bradford Council Web Team";

        MimeMessage email = createEmail(toEmail, "mustafakamran491@gmail.com", subject, body);
        sendMessage(email);
    }

}
