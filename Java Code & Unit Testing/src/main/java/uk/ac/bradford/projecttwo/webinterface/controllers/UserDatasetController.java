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
import uk.ac.bradford.projecttwo.webinterface.services.LoginServiceImpl;
import uk.ac.bradford.projecttwo.webinterface.services.UploadDatasetService;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user/datasets")
public class UserDatasetController {

    private final DatasetMetadataService datasetMetadataService;
    private final UploadDatasetService uploadDatasetService;

    @Autowired
    private RegistrationRepositoryImpl registrationRepository;

    @Autowired
    private DatasetAccessRequestServiceImpl datasetAccessRequestService;

    @Autowired
    public UserDatasetController(DatasetMetadataService datasetMetadataService,
                                 UploadDatasetService uploadDatasetService) {
        this.datasetMetadataService = datasetMetadataService;
        this.uploadDatasetService = uploadDatasetService;
    }

    @GetMapping("/view/{name}")
    public String viewDataset(@PathVariable("name") String datasetName, Model model) {
        List<Map<String, Object>> data = uploadDatasetService.getDatasetContent(datasetName);
        model.addAttribute("datasetName", datasetName);
        model.addAttribute("data", data);
        return "user/view_dataset";
    }

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

        // ✅ Update both flags
        if (userEmail != null) {
            for (DatasetMetadataModel metadata : datasets) {
                boolean accessGranted = datasetAccessRequestService.isApproved(metadata.getDatasetName(), userEmail);
                boolean alreadyRequested = datasetAccessRequestService.hasUserAlreadyRequested(metadata.getDatasetName(), userEmail);
                metadata.setApproved(accessGranted); // true only if status is APPROVED
                metadata.setRequested(alreadyRequested); // true for both PENDING and APPROVED
            }
        }

        return "user/dataset_list";
    }



}
