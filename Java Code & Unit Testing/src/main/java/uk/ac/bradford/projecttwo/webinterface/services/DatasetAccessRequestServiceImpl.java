package uk.ac.bradford.projecttwo.webinterface.services;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.bradford.projecttwo.webinterface.models.DatasetAccessRequestModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.DatasetAccessRequestRepository;

import java.util.List;

/**
 * Implementation of {@link DatasetAccessRequestService} for managing
 * dataset access requests. Handles CRUD operations and email notifications.
 */
@Service
public class DatasetAccessRequestServiceImpl implements DatasetAccessRequestService {

    private final DatasetAccessRequestRepository repository;
    private final EmailService emailService;

    /**
     * Constructor for dependency injection.
     *
     * @param repository    The dataset access request repository.
     * @param emailService  The service used to send email notifications.
     */
    @Autowired
    public DatasetAccessRequestServiceImpl(DatasetAccessRequestRepository repository, EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DatasetAccessRequestModel> getAllAccessRequests() {
        return repository.getAllRequests();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean updateRequestStatus(int requestId, String newStatus) {
        return repository.updateRequestStatus(requestId, newStatus);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasUserAlreadyRequested(String datasetName, String email) {
        return repository.existsByDatasetAndUser(datasetName, email);
    }

    /**
     * {@inheritDoc}
     * Sends notification emails to both user and admin upon request submission.
     */
    @Override
    public void saveAccessRequest(DatasetAccessRequestModel request) {
        repository.saveRequest(request);

        // Notify user
        try {
            MimeMessage userEmail = emailService.createEmail(
                    request.getRequestedBy(),
                    "mustafakamran491@gmail.com",
                    "Dataset Access Request Submitted",
                    "Hi,\n\nYour request to access the dataset '" +
                            request.getDatasetName() + "' has been successfully submitted.\n" +
                            "Please wait while the admin reviews and approves it.\n\n" +
                            "Regards,\nCity of Bradford Web Team"
            );
            emailService.sendMessage(userEmail);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Notify admin
        try {
            MimeMessage adminEmail = emailService.createEmail(
                    "mustafakamran491@gmail.com",
                    "mustafakamran491@gmail.com",
                    "Dataset Access Request Received",
                    "User " + request.getRequestedBy() +
                            " has requested access to dataset '" +
                            request.getDatasetName() +
                            "'.\nPlease review it in the permissions panel."
            );
            emailService.sendMessage(adminEmail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DatasetAccessRequestModel getRequestByDatasetAndEmail(String datasetName, String email) {
        return repository.findByDatasetNameAndEmail(datasetName, email);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRequestEmailById(int requestId) {
        return repository.getRequestEmailById(requestId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isApproved(String datasetName, String userEmail) {
        DatasetAccessRequestModel request = repository.findByDatasetNameAndEmail(datasetName, userEmail);
        return request != null && "APPROVED".equalsIgnoreCase(request.getStatus());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DatasetAccessRequestModel> searchDatasetRequests(String email, String datasetName, String department, String status) {
        return repository.searchDatasetRequests(email, datasetName, department, status);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DatasetAccessRequestModel> getRequestsByEmail(String email) {
        return repository.findRequestsByEmail(email);
    }
}
