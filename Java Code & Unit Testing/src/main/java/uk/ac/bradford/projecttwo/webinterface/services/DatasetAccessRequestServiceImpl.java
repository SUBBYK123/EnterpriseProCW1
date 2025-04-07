package uk.ac.bradford.projecttwo.webinterface.services;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.bradford.projecttwo.webinterface.models.DatasetAccessRequestModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.DatasetAccessRequestRepository;

import java.util.List;

@Service
public class DatasetAccessRequestServiceImpl implements DatasetAccessRequestService{


    private final DatasetAccessRequestRepository repository;

    private final EmailService emailService;

    @Autowired
    public DatasetAccessRequestServiceImpl(DatasetAccessRequestRepository repository, EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }

    @Override
    public List<DatasetAccessRequestModel> getAllAccessRequests() {
        return repository.getAllRequests();
    }

    @Override
    public boolean updateRequestStatus(int requestId, String newStatus) {
        return repository.updateRequestStatus(requestId,newStatus);
    }

    @Override
    public boolean hasUserAlreadyRequested(String datasetName, String email) {
        return repository.existsByDatasetAndUser(datasetName, email);
    }

    @Override
    public void saveAccessRequest(DatasetAccessRequestModel request) {
        repository.saveRequest(request);

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

    @Override
    public DatasetAccessRequestModel getRequestByDatasetAndEmail(String datasetName, String email) {
        return repository.findByDatasetNameAndEmail(datasetName, email);
    }

    @Override
    public String getRequestEmailById(int requestId) {
        return repository.getRequestEmailById(requestId);
    }

    @Override
    public boolean isApproved(String datasetName, String userEmail) {
        DatasetAccessRequestModel request = repository.findByDatasetNameAndEmail(datasetName, userEmail);
        return request != null && "APPROVED".equalsIgnoreCase(request.getStatus());
    }

}
