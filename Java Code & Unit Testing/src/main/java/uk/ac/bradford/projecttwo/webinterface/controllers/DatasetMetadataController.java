package uk.ac.bradford.projecttwo.webinterface.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.ac.bradford.projecttwo.webinterface.models.DatasetAccessRequestModel;
import uk.ac.bradford.projecttwo.webinterface.models.DatasetMetadataModel;
import uk.ac.bradford.projecttwo.webinterface.models.RegistrationModel;
import uk.ac.bradford.projecttwo.webinterface.models.UploadDatasetModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.RegistrationRepositoryImpl;
import uk.ac.bradford.projecttwo.webinterface.services.DatasetAccessRequestServiceImpl;
import uk.ac.bradford.projecttwo.webinterface.services.DatasetMetadataService;
import uk.ac.bradford.projecttwo.webinterface.services.LoginServiceImpl;
import uk.ac.bradford.projecttwo.webinterface.services.UploadDatasetService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Controller to manage dataset-related operations for the users and admin,
 * including uploading datasets, searching datasets, downloading datasets, and handling dataset access requests.
 */
@Controller
@RequestMapping("/datasets")
public class DatasetMetadataController {

    private final UploadDatasetService uploadDatasetService;
    private final DatasetMetadataService datasetMetadataService;
    private final LoginServiceImpl loginService;
    private final RegistrationRepositoryImpl registrationRepository;
    private final DatasetAccessRequestServiceImpl datasetAccessRequestService;

    /**
     * Constructor for dependency injection.
     *
     * @param uploadDatasetService Service for dataset uploads.
     * @param datasetMetadataService Service for handling dataset metadata.
     * @param loginService Service for managing login operations.
     * @param registrationRepository Repository for user registration data.
     * @param datasetAccessRequestService Service for handling dataset access requests.
     */
    @Autowired
    public DatasetMetadataController(UploadDatasetService uploadDatasetService,
                                     DatasetMetadataService datasetMetadataService,
                                     LoginServiceImpl loginService,
                                     RegistrationRepositoryImpl registrationRepository,
                                     DatasetAccessRequestServiceImpl datasetAccessRequestService) {
        this.uploadDatasetService = uploadDatasetService;
        this.datasetMetadataService = datasetMetadataService;
        this.loginService = loginService;
        this.registrationRepository = registrationRepository;
        this.datasetAccessRequestService = datasetAccessRequestService;
    }

    /**
     * Displays the index page of datasets for the user, including user details if logged in.
     *
     * @param model The model to hold attributes for the view.
     * @param principal The currently authenticated user.
     * @return The name of the view to display the datasets index.
     */
    @GetMapping("/index")
    public String indexPage(Model model, Principal principal) {
        if (principal != null) {
            String email = principal.getName();
            RegistrationModel user = registrationRepository.findUserByEmail(email);

            if (user != null) {
                model.addAttribute("loggedEmail", user.getEmailAddress());
                model.addAttribute("loggedDepartment", user.getDepartment());
            } else {
                model.addAttribute("loggedEmail", email);
                model.addAttribute("loggedDepartment", "Unknown");
            }
        }
        return "index";
    }

    /**
     * Handles the uploading of a dataset, including processing the dataset metadata and content.
     *
     * @param datasetName The name of the dataset.
     * @param department The department associated with the dataset.
     * @param uploadedBy The user uploading the dataset.
     * @param role The role of the user uploading the dataset.
     * @param columnsJson JSON string containing dataset column names.
     * @param dataJson JSON string containing dataset content.
     * @param file The file containing the dataset.
     * @param principal The currently authenticated user.
     * @return A ResponseEntity with the result of the upload process.
     */
    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<String> uploadDataset(
            @RequestParam("datasetName") String datasetName,
            @RequestParam("department") String department,
            @RequestParam("uploadedBy") String uploadedBy,
            @RequestParam("role") String role,
            @RequestParam("columns") String columnsJson,
            @RequestParam("data") String dataJson,
            @RequestParam(value = "file", required = false) MultipartFile file,
            Principal principal) {

        try {
            ObjectMapper mapper = new ObjectMapper();

            List<String> columns = mapper.readValue(columnsJson, new TypeReference<>() {});
            List<Map<String, Object>> data = mapper.readValue(dataJson, new TypeReference<>() {});

            // Override department and uploadedBy if logged in
            String finalEmail = uploadedBy;
            String finalDepartment = department;

            if (principal != null) {
                String email = principal.getName();
                RegistrationModel user = registrationRepository.findUserByEmail(email);
                if (user != null) {
                    finalEmail = user.getEmailAddress();
                    finalDepartment = user.getDepartment();
                }
            }

            // Upload dataset
            UploadDatasetModel uploadModel = new UploadDatasetModel();
            uploadModel.setDatasetName(datasetName);
            uploadModel.setDepartment(finalDepartment);
            uploadModel.setUploadedBy(finalEmail);
            uploadModel.setRole(role);
            uploadModel.setColumns(columns);
            uploadModel.setData(data);

            boolean success = uploadDatasetService.processDatasetUpload(uploadModel);

            if (success) {
                DatasetMetadataModel metadata = new DatasetMetadataModel();
                metadata.setDatasetName(datasetName);
                metadata.setDepartment(finalDepartment);
                metadata.setUploadedBy(finalEmail);
                metadata.setRole(role);
                metadata.setUploadDate(LocalDateTime.now());

                if (!datasetMetadataService.isDuplicateDataset(datasetName, uploadedBy)) {
                    datasetMetadataService.storeDatasetMetadata(metadata);
                }

                return ResponseEntity.ok("✅ Dataset uploaded and metadata saved.");
            } else {
                return ResponseEntity.status(500).body("❌ Upload failed.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("❌ Error parsing data: " + e.getMessage());
        }
    }

    /**
     * Handles the downloading of a dataset for an authorized user.
     * Verifies that the user has been granted access to the dataset.
     *
     * @param datasetName The name of the dataset to download.
     * @param principal The currently authenticated user.
     * @return A ResponseEntity containing the dataset file if the user has access, otherwise an error message.
     */
    @GetMapping("/download/{datasetName}")
    public ResponseEntity<?> downloadDataset(@PathVariable String datasetName, Principal principal) {
        String userEmail = principal.getName();

        // Check if the user has approved access
        DatasetAccessRequestModel request = datasetAccessRequestService.getRequestByDatasetAndEmail(datasetName, userEmail);

        if (request == null || !"APPROVED".equalsIgnoreCase(request.getStatus())) {
            return ResponseEntity.status(403).body("You don't have permission to download this dataset.");
        }

        // Path to the dataset file (assumes it's saved in a local 'datasets' folder)
        Path path = Paths.get("src/main/resources/datasets/download", datasetName + ".csv");
        if (!Files.exists(path)) {
            return ResponseEntity.status(404).body("Dataset not found.");
        }

        try {
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + path.getFileName().toString() + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(resource.contentLength())
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error while reading the file.");
        }
    }

    /**
     * Handles the upload of a dataset using streaming.
     *
     * @param file The file containing the dataset.
     * @param datasetName The name of the dataset.
     * @param department The department associated with the dataset.
     * @param uploadedBy The user uploading the dataset.
     * @param role The role of the user uploading the dataset.
     * @param principal The currently authenticated user.
     * @return A ResponseEntity indicating the result of the upload operation.
     */
    @PostMapping("/upload-stream")
    @ResponseBody
    public ResponseEntity<String> uploadDatasetStream(
            @RequestParam("file") MultipartFile file,
            @RequestParam("datasetName") String datasetName,
            @RequestParam("department") String department,
            @RequestParam("uploadedBy") String uploadedBy,
            @RequestParam("role") String role,
            Principal principal) {

        try {
            String finalEmail = uploadedBy;
            String finalDepartment = department;

            if (principal != null) {
                String email = principal.getName();
                RegistrationModel user = registrationRepository.findUserByEmail(email);
                if (user != null) {
                    finalEmail = user.getEmailAddress();
                    finalDepartment = user.getDepartment();
                }
            }

            boolean success = uploadDatasetService.uploadDatasetStreamed(datasetName, finalDepartment, finalEmail, role, file);

            if (success) {
                DatasetMetadataModel metadata = new DatasetMetadataModel();
                metadata.setDatasetName(datasetName);
                metadata.setDepartment(finalDepartment);
                metadata.setUploadedBy(finalEmail);
                metadata.setRole(role);
                metadata.setUploadDate(LocalDateTime.now());

                if (!datasetMetadataService.isDuplicateDataset(datasetName, finalEmail)) {
                    datasetMetadataService.storeDatasetMetadata(metadata);
                }

                return ResponseEntity.ok("✅ Dataset uploaded via stream.");
            } else {
                return ResponseEntity.status(500).body("❌ Failed to upload.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("❌ Error uploading file: " + e.getMessage());
        }
    }

    /**
     * Searches and filters datasets based on keyword, department, and role.
     *
     * @param keyword The keyword to search for in dataset names.
     * @param department The department to filter datasets by.
     * @param role The role to filter datasets by.
     * @param model The model to hold attributes for the view.
     * @param principal The currently authenticated user.
     * @return The view name to display the filtered list of datasets.
     */
    @GetMapping("/search")
    public String searchDatasets(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String role,
            Model model,
            Principal principal
    ) {
        List<DatasetMetadataModel> filtered = datasetMetadataService.searchAndFilter(keyword, department, role);

        String userEmail = null;

        if (principal != null) {
            userEmail = principal.getName();
            RegistrationModel user = registrationRepository.findUserByEmail(userEmail);
            if (user != null) {
                model.addAttribute("loggedEmail", user.getEmailAddress());
                model.addAttribute("loggedDepartment", user.getDepartment());
            }
        }

        // 🔁 Re-assign access flags on filtered list
        if (userEmail != null) {
            for (DatasetMetadataModel metadata : filtered) {
                boolean accessGranted = datasetAccessRequestService.isApproved(metadata.getDatasetName(), userEmail);
                boolean alreadyRequested = datasetAccessRequestService.hasUserAlreadyRequested(metadata.getDatasetName(), userEmail);

                metadata.setApproved(accessGranted);
                metadata.setRequested(alreadyRequested);
            }
        }

        model.addAttribute("datasets", filtered);
        return "user/dataset_list";
    }

    /**
     * Handles the request for access to a dataset by the user.
     *
     * @param datasetName The name of the dataset to request access for.
     * @param principal The currently authenticated user.
     * @param redirectAttributes The redirect attributes to hold flash messages.
     * @return Redirects to the dataset list view.
     */
    @PostMapping("/request-access")
    public String handleAccessRequest(
            @RequestParam("datasetName") String datasetName,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        if (principal == null) {
            redirectAttributes.addFlashAttribute("error", "Please log in to request access.");
            return "redirect:/user/datasets/list";
        }

        String email = principal.getName();
        RegistrationModel user = registrationRepository.findUserByEmail(email);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "User not found.");
            return "redirect:/user/datasets/list";
        }

        boolean alreadyRequested = datasetAccessRequestService.hasUserAlreadyRequested(datasetName, email);
        if (alreadyRequested) {
            redirectAttributes.addFlashAttribute("error", "Access already requested for this dataset.");
            return "redirect:/user/datasets/list";
        }

        DatasetAccessRequestModel request = new DatasetAccessRequestModel();
        request.setRequestedBy(email);
        request.setDatasetName(datasetName);
        request.setDepartment(user.getDepartment());
        request.setRole("ROLE_USER");
        request.setStatus("PENDING");
        request.setRequestDate(LocalDateTime.now());

        datasetAccessRequestService.saveAccessRequest(request);
        redirectAttributes.addFlashAttribute("success", "Access request submitted successfully.");

        return "redirect:/user/datasets/list";
    }
}
