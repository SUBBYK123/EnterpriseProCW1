package uk.ac.bradford.projecttwo.webinterface.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

/**
 * Service class for authenticating and obtaining an authorized Gmail API service instance.
 */
public class GmailService {

    private static final String APPLICATION_NAME = "Password Reset Service";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "/tokens";

    // Scope required for sending emails via Gmail API
    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_SEND);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Obtains OAuth 2.0 credentials for the Gmail API.
     * @param HTTP_TRANSPORT The transport used to communicate with Google APIs.
     * @return An authorized Credential object.
     * @throws IOException If an error occurs while loading credentials.
     */
    public static Credential getCredentials(final HttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = GmailService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not Found: " + CREDENTIALS_FILE_PATH);
        }

        // Load client secrets from the credentials.json file
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build an authorization flow to handle OAuth 2.0 authentication
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        // Returns an authorized Credential object after user authentication
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver.Builder()
                .setHost("localhost")
                .setPort(8080) // Change this port if necessary
                .build()).authorize("user");
    }

    /**
     * Initializes and returns an authorized Gmail service instance.
     * @return A Gmail service object used for sending emails.
     * @throws GeneralSecurityException If a security-related error occurs.
     * @throws IOException If an error occurs while setting up the Gmail service.
     */
    public static Gmail getGmailService() throws GeneralSecurityException, IOException {
        HttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = getCredentials(HTTP_TRANSPORT);

        return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}