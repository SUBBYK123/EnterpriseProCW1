package uk.ac.bradford.projecttwo.webinterface.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/datasets")
public class DatasetMetadataController {

    @Autowired
    private UploadDatasetService uploadDatasetService;

    @Autowired
    private DatasetMetadataService datasetMetadataService;

    @Autowired
    private LoginServiceImpl loginService;

    @Autowired
    private RegistrationRepositoryImpl registrationRepository;

    @Autowired
    private DatasetAccessRequestServiceImpl datasetAccessRequestService;

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

        // Mark dataset as approved if user has permission
        if (userEmail != null) {
            for (DatasetMetadataModel metadata : datasets) {
                boolean accessGranted = datasetAccessRequestService.isApproved(metadata.getDatasetName(), userEmail);
                metadata.setApproved(accessGranted);
            }
        }

        return "dataset_list";
    }


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

    @GetMapping("/download/{datasetName}")
    public ResponseEntity<?> downloadDataset(@PathVariable String datasetName, Principal principal) {
        String userEmail = principal.getName();

        // Check if the user has approved access (you must have this method implemented)
        DatasetAccessRequestModel request = datasetAccessRequestService.getRequestByDatasetAndEmail(datasetName, userEmail);

        if (request == null || !"APPROVED".equalsIgnoreCase(request.getStatus())) {
            return ResponseEntity.status(403).body("You don't have permission to download this dataset.");
        }

        // Path to the dataset file (assumes it's saved in a local 'datasets' folder)
        Path path = Paths.get("datasets", datasetName + ".csv");

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
}
