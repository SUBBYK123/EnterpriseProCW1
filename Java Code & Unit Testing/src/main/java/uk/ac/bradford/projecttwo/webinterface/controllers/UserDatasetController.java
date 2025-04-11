package uk.ac.bradford.projecttwo.webinterface.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uk.ac.bradford.projecttwo.webinterface.models.DatasetAccessRequestModel;
import uk.ac.bradford.projecttwo.webinterface.models.DatasetMetadataModel;
import uk.ac.bradford.projecttwo.webinterface.models.RegistrationModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.RegistrationRepositoryImpl;
import uk.ac.bradford.projecttwo.webinterface.services.DatasetAccessRequestServiceImpl;
import uk.ac.bradford.projecttwo.webinterface.services.DatasetMetadataService;
import uk.ac.bradford.projecttwo.webinterface.services.UploadDatasetService;

import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * Controller responsible for managing user interactions with datasets,
 * including viewing datasets, requesting access, and listing datasets.
 */
@Controller
@RequestMapping("/user/datasets")
public class UserDatasetController {

    private final DatasetMetadataService datasetMetadataService;
    private final UploadDatasetService uploadDatasetService;

    @Autowired
    private RegistrationRepositoryImpl registrationRepository;

    @Autowired
    private DatasetAccessRequestServiceImpl datasetAccessRequestService;

    /**
     * Constructor to initialize the controller with the necessary services.
     *
     * @param datasetMetadataService The service for managing dataset metadata.
     * @param uploadDatasetService   The service for handling dataset uploads.
     */
    @Autowired
    public UserDatasetController(DatasetMetadataService datasetMetadataService,
                                 UploadDatasetService uploadDatasetService) {
        this.datasetMetadataService = datasetMetadataService;
        this.uploadDatasetService = uploadDatasetService;
    }

    /**
     * Displays the details of a specific dataset.
     *
     * @param datasetName The name of the dataset to view.
     * @param model       The model to pass data to the view.
     * @return The name of the view to render ("user/view_dataset").
     */
    @GetMapping("/view/{name}")
    public String viewDataset(@PathVariable("name") String datasetName, Model model) {
        List<Map<String, Object>> data = uploadDatasetService.getDatasetContent(datasetName);
        model.addAttribute("datasetName", datasetName);
        model.addAttribute("data", data);
        return "user/view_dataset";
    }

    /**
     * Lists all datasets available to the user and displays their access status.
     *
     * @param model     The model to pass data to the view.
     * @param principal The principal object containing information about the logged-in user.
     * @return The name of the view to render ("user/dataset_list").
     */
    @GetMapping("/list")
    public String listDatasets(Model model, Principal principal) {
        List<DatasetMetadataModel> datasets = datasetMetadataService.fetchAllMetadata();
        model.addAttribute("datasets", datasets);

        String userEmail = null;

        if (principal != null) {
            userEmail = principal.getName();
            RegistrationModel user = registrationRepository.findUserByEmail(userEmail);
            if (user != null) {
                model.addAttribute("loggedEmail", user.getEmailAddress());
                model.addAttribute("loggedDepartment", user.getDepartment());
            }
        }

        // Update access and request status for datasets
        if (userEmail != null) {
            for (DatasetMetadataModel metadata : datasets) {
                boolean accessGranted = datasetAccessRequestService.isApproved(metadata.getDatasetName(), userEmail);
                boolean alreadyRequested = datasetAccessRequestService.hasUserAlreadyRequested(metadata.getDatasetName(), userEmail);
                metadata.setApproved(accessGranted); // True only if status is APPROVED
                metadata.setRequested(alreadyRequested); // True for both PENDING and APPROVED
            }
        }

        return "user/dataset_list";
    }
}
