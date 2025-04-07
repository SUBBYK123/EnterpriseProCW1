package uk.ac.bradford.projecttwo.webinterface.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.bradford.projecttwo.webinterface.models.DatasetMetadataModel;
import uk.ac.bradford.projecttwo.webinterface.models.RegistrationModel;
import uk.ac.bradford.projecttwo.webinterface.models.UploadDatasetModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.RegistrationRepositoryImpl;
import uk.ac.bradford.projecttwo.webinterface.services.DatasetMetadataService;
import uk.ac.bradford.projecttwo.webinterface.services.LoginServiceImpl;
import uk.ac.bradford.projecttwo.webinterface.services.UploadDatasetService;

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

    @GetMapping("/list")
    public String listDatasets(Model model, Principal principal) {
        List<DatasetMetadataModel> datasets = datasetMetadataService.fetchAllMetadata();
        model.addAttribute("datasets", datasets);

        if (principal != null) {
            String email = principal.getName();
            RegistrationModel user = registrationRepository.findUserByEmail(email);
            if (user != null) {
                model.addAttribute("loggedEmail", user.getEmailAddress());
                model.addAttribute("loggedDepartment", user.getDepartment());
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
}
