package uk.ac.bradford.projecttwo.webinterface.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.ac.bradford.projecttwo.webinterface.models.DatasetMetadataModel;
import uk.ac.bradford.projecttwo.webinterface.services.DatasetMetadataService;
import uk.ac.bradford.projecttwo.webinterface.services.UploadDatasetService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/datasets")
public class AdminDatasetController {

    private final DatasetMetadataService datasetMetadataService;
    private final UploadDatasetService uploadDatasetService;

    @Autowired
    public AdminDatasetController(DatasetMetadataService datasetMetadataService,
                                  UploadDatasetService uploadDatasetService) {
        this.datasetMetadataService = datasetMetadataService;
        this.uploadDatasetService = uploadDatasetService;
    }

    // Admin list view
    @GetMapping
    public String showAdminDatasets(Model model) {
        List<DatasetMetadataModel> datasets = datasetMetadataService.fetchAllMetadata();
        model.addAttribute("datasets", datasets);
        return "admin/admin_dataset_list";
    }

    // View dataset content
    @GetMapping("/view/{name}")
    public String viewDataset(@PathVariable("name") String datasetName, Model model) {
        List<Map<String, Object>> data = uploadDatasetService.getDatasetContent(datasetName);
        model.addAttribute("datasetName", datasetName);
        model.addAttribute("data", data);
        return "admin/view_dataset";
    }

    // Edit dataset metadata (e.g., name/department)
    @GetMapping("/edit/{name}")
    public String editDatasetForm(@PathVariable("name") String datasetName, Model model) {
        DatasetMetadataModel metadata = datasetMetadataService.getMetadataByName(datasetName);
        model.addAttribute("dataset", metadata);
        return "admin/edit_dataset";
    }

    @PostMapping("/edit/{name}")
    public String editDatasetSubmit(@PathVariable("name") String datasetName,
                                    @ModelAttribute DatasetMetadataModel updatedMetadata,
                                    RedirectAttributes redirectAttributes) {
        datasetMetadataService.updateDatasetMetadata(datasetName, updatedMetadata);
        redirectAttributes.addFlashAttribute("success", "Dataset updated successfully.");
        return "redirect:/admin/datasets";
    }

    // Delete dataset
    @PostMapping("/delete/{name}")
    public String deleteDataset(@PathVariable("name") String datasetName,
                                RedirectAttributes redirectAttributes) {
        try {
            datasetMetadataService.deleteDatasetByName(datasetName);
            uploadDatasetService.deleteDatasetFile(datasetName);
            redirectAttributes.addFlashAttribute("success", "Dataset deleted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error deleting dataset.");
        }
        return "redirect:/admin/datasets";
    }
}
