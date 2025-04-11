package uk.ac.bradford.projecttwo.webinterface.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.ac.bradford.projecttwo.webinterface.models.DatasetAccessRequestModel;
import uk.ac.bradford.projecttwo.webinterface.models.PermissionRequestModel;
import uk.ac.bradford.projecttwo.webinterface.models.RegistrationModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.RegistrationRepositoryImpl;
import uk.ac.bradford.projecttwo.webinterface.services.DatasetAccessRequestService;
import uk.ac.bradford.projecttwo.webinterface.services.LogServiceImpl;
import uk.ac.bradford.projecttwo.webinterface.services.PermissionRequestService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin/permissions")
public class PermissionController {

    private final DatasetAccessRequestService accessRequestService;
    private final RegistrationRepositoryImpl registrationRepository;
    private final LogServiceImpl logService;
    private final PermissionRequestService permissionRequestService;

    @Autowired
    public PermissionController(DatasetAccessRequestService accessRequestService,
                                RegistrationRepositoryImpl registrationRepository,
                                LogServiceImpl logService,
                                PermissionRequestService permissionRequestService) {
        this.accessRequestService = accessRequestService;
        this.registrationRepository = registrationRepository;
        this.logService = logService;
        this.permissionRequestService = permissionRequestService;
    }

    /**
     * Show both dataset and role/department permission requests
     */
    @GetMapping
    public String showPermissionRequests(
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "dataset", required = false) String dataset,
            @RequestParam(value = "department", required = false) String department,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "success", required = false) String success,
            @RequestParam(value = "error", required = false) String error,
            Model model
    ) {
        List<DatasetAccessRequestModel> datasetRequests;
        List<PermissionRequestModel> roleRequests;

        boolean hasFilters = email != null || dataset != null || department != null || status != null;

        // If filters are present, search using them; otherwise, fetch all
        if (hasFilters) {
            datasetRequests = accessRequestService.searchDatasetRequests(email, dataset, department, status);
            roleRequests = permissionRequestService.searchPermissionRequests(email, dataset, department, status);
        } else {
            datasetRequests = accessRequestService.getAllAccessRequests();
            roleRequests = permissionRequestService.getAllRequests();
        }

        model.addAttribute("datasetRequests", datasetRequests);
        model.addAttribute("roleRequests", roleRequests);

        if (success != null) model.addAttribute("success", success);
        if (error != null) model.addAttribute("error", error);

        return "admin/permissions";
    }


    /**
     * Approve dataset access request
     */
    @PostMapping("/approve-dataset/{id}")
    public String approveDatasetRequest(@PathVariable("id") int requestId, RedirectAttributes redirectAttributes) {
        try {
            accessRequestService.updateRequestStatus(requestId, "APPROVED");
            String email = accessRequestService.getRequestEmailById(requestId);
            logService.log(email, "APPROVE DATASET", "SUCCESS");
            redirectAttributes.addFlashAttribute("success", "Dataset access approved.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error approving dataset access.");
        }
        return "redirect:/admin/permissions"; // ✅
    }

    @PostMapping("/deny-dataset/{id}")
    public String denyDatasetRequest(@PathVariable("id") int requestId, RedirectAttributes redirectAttributes) {
        try {
            accessRequestService.updateRequestStatus(requestId, "DENIED");
            String email = accessRequestService.getRequestEmailById(requestId);
            logService.log(email, "DENY DATASET", "SUCCESS");
            redirectAttributes.addFlashAttribute("error", "Dataset access denied.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error denying dataset access.");
        }
        return "redirect:/admin/permissions"; // ✅
    }

    @PostMapping("/approve/{id}")
    public String approveRoleRequest(@PathVariable("id") int requestId, RedirectAttributes redirectAttributes) {
        try {
            permissionRequestService.approveRequest(requestId);
            String email = permissionRequestService.getRequestEmailById(requestId);
            logService.log(email, "APPROVE ROLE", "SUCCESS");
            redirectAttributes.addFlashAttribute("success", "Permission request approved.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error approving role/department request.");
        }
        return "redirect:/admin/permissions"; // ✅
    }

    @PostMapping("/deny/{id}")
    public String denyRoleRequest(@PathVariable("id") int requestId, RedirectAttributes redirectAttributes) {
        try {
            permissionRequestService.denyRequest(requestId);
            String email = permissionRequestService.getRequestEmailById(requestId);
            logService.log(email, "DENY ROLE", "SUCCESS");
            redirectAttributes.addFlashAttribute("error", "Permission request denied.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error denying role/department request.");
        }
        return "redirect:/admin/permissions"; // ✅
    }


}
