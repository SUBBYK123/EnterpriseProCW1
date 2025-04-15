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

/**
 * Controller for handling dataset management functionality for the admin.
 * It allows viewing, editing, and deleting datasets along with viewing their content.
 */
@Controller
@RequestMapping("/admin/datasets")
public class AdminDatasetController {

    private final DatasetMetadataService datasetMetadataService;
    private final UploadDatasetService uploadDatasetService;

    /**
     * Constructor to initialize the services needed for managing datasets.
     *
     * @param datasetMetadataService Service for handling dataset metadata operations.
     * @param uploadDatasetService Service for handling dataset file upload and retrieval operations.
     */
    @Autowired
    public AdminDatasetController(DatasetMetadataService datasetMetadataService,
                                  UploadDatasetService uploadDatasetService) {
        this.datasetMetadataService = datasetMetadataService;
        this.uploadDatasetService = uploadDatasetService;
    }

    /**
     * Displays the list of datasets for the admin.
     *
     * @param model The model that holds data for the view.
     * @return The view name to display the list of datasets.
     */
    @GetMapping
    public String showAdminDatasets(@RequestParam(required = false) String keyword,
                                    @RequestParam(required = false) String department,
                                    @RequestParam(required = false) String role,
                                    Model model) {
        List<DatasetMetadataModel> datasets;

        if (keyword != null || department != null || role != null) {
            datasets = datasetMetadataService.searchAndFilter(keyword, department, role);
        } else {
            datasets = datasetMetadataService.fetchAllMetadata();
        }

        model.addAttribute("datasets", datasets);
        return "admin/admin_dataset_list";
    }


    /**
     * Displays the content of a specific dataset.
     *
     * @param datasetName The name of the dataset to view.
     * @param model The model that holds data for the view.
     * @return The view name to display the dataset content.
     */
    @GetMapping("/view/{name}")
    public String viewDataset(@PathVariable("name") String datasetName, Model model) {
        List<Map<String, Object>> data = uploadDatasetService.getDatasetContent(datasetName);
        model.addAttribute("datasetName", datasetName);
        model.addAttribute("data", data);
        return "admin/view_dataset";
    }

    /**
     * Displays the form for editing the metadata of a dataset.
     *
     * @param datasetName The name of the dataset to edit.
     * @param model The model that holds data for the view.
     * @return The view name to display the dataset editing form.
     */
    @GetMapping("/edit/{name}")
    public String editDatasetForm(@PathVariable("name") String datasetName, Model model) {
        DatasetMetadataModel metadata = datasetMetadataService.getMetadataByName(datasetName);
        model.addAttribute("dataset", metadata);
        return "admin/edit_dataset";
    }

    /**
     * Handles the submission of the edited dataset metadata.
     *
     * @param datasetName The name of the dataset to update.
     * @param updatedMetadata The updated dataset metadata.
     * @param redirectAttributes Holds the flash attributes for redirecting.
     * @return Redirects to the dataset list view.
     */
    @PostMapping("/edit/{name}")
    public String editDatasetSubmit(@PathVariable("name") String datasetName,
                                    @ModelAttribute DatasetMetadataModel updatedMetadata,
                                    RedirectAttributes redirectAttributes) {
        datasetMetadataService.updateDatasetMetadata(datasetName, updatedMetadata);
        redirectAttributes.addFlashAttribute("success", "Dataset updated successfully.");
        return "redirect:/admin/datasets";
    }

    /**
     * Handles the deletion of a dataset.
     *
     * @param datasetName The name of the dataset to delete.
     * @param redirectAttributes Holds the flash attributes for redirecting.
     * @return Redirects to the dataset list view.
     */
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
