package uk.ac.bradford.projecttwo.webinterface.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.bradford.projecttwo.webinterface.models.DatasetRequest;
import uk.ac.bradford.projecttwo.webinterface.services.DatasetService;

@RestController
@RequestMapping("/api")
public class DatasetController {

    @Autowired
    private DatasetService datasetService;

    @PostMapping("/upload-dataset")
    public ResponseEntity<String> uploadDataset(@RequestBody DatasetRequest datasetRequest) {
        try {
            datasetService.createTableAndSaveData(datasetRequest);
            return ResponseEntity.ok("Dataset uploaded successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error uploading dataset: " + e.getMessage());
        }
    }
}
