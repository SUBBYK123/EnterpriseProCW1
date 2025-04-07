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
import uk.ac.bradford.projecttwo.webinterface.models.UploadDatasetModel;
import uk.ac.bradford.projecttwo.webinterface.services.DatasetMetadataService;
import uk.ac.bradford.projecttwo.webinterface.services.UploadDatasetService;

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

    @GetMapping("/list")
    public String listDatasets(Model model) {
        List<DatasetMetadataModel> datasets = datasetMetadataService.fetchAllMetadata();
        model.addAttribute("datasets", datasets);
        return "dataset_list";
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
            @RequestParam(value = "file", required = false) MultipartFile file) {

        try {
            ObjectMapper mapper = new ObjectMapper();

            List<String> columns = mapper.readValue(columnsJson, new TypeReference<>() {});
            List<Map<String, Object>> data = mapper.readValue(dataJson, new TypeReference<>() {});

            // Upload dataset (create table, insert data)
            UploadDatasetModel uploadModel = new UploadDatasetModel();
            uploadModel.setDatasetName(datasetName);
            uploadModel.setDepartment(department);
            uploadModel.setUploadedBy(uploadedBy);
            uploadModel.setRole(role);
            uploadModel.setColumns(columns);
            uploadModel.setData(data);

            boolean success = uploadDatasetService.processDatasetUpload(uploadModel);

            if (success) {
                DatasetMetadataModel metadata = new DatasetMetadataModel();
                metadata.setDatasetName(datasetName);
                metadata.setDepartment(department);
                metadata.setUploadedBy(uploadedBy);
                metadata.setRole(role);
                metadata.setUploadDate(LocalDateTime.now());

                datasetMetadataService.storeDatasetMetadata(metadata);

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
