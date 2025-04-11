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

/**
 * Controller for managing permission requests in the admin section.
 * This class handles viewing, approving, and denying dataset access and role/department permission requests.
 */
@Controller
@RequestMapping("/admin/permissions")
public class PermissionController {

    private final DatasetAccessRequestService accessRequestService;
    private final RegistrationRepositoryImpl registrationRepository;
    private final LogServiceImpl logService;
    private final PermissionRequestService permissionRequestService;

    /**
     * Constructor for initializing the PermissionController with required services.
     *
     * @param accessRequestService Service to manage dataset access requests.
     * @param registrationRepository Repository to access user registration data.
     * @param logService Service to log actions performed in the system.
     * @param permissionRequestService Service to manage role/department permission requests.
     */
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
     * Displays both dataset and role/department permission requests.
     * Allows filtering by email, dataset, department, and status.
     *
     * @param email Filter by email address (optional).
     * @param dataset Filter by dataset name (optional).
     * @param department Filter by department (optional).
     * @param status Filter by status (optional).
     * @param success Message for successful actions (optional).
     * @param error Message for errors (optional).
     * @param model The model object to pass data to the view.
     * @return The name of the view template to render ("admin/permissions").
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

        return "admin/permissions"; // Render the permissions view
    }

    /**
     * Approves a dataset access request.
     *
     * @param requestId The ID of the request to approve.
     * @param redirectAttributes The attributes used to redirect and show success/error messages.
     * @return Redirects to the permissions page with a success or error message.
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
        return "redirect:/admin/permissions"; // Redirect back to the permissions page
    }

    /**
     * Denies a dataset access request.
     *
     * @param requestId The ID of the request to deny.
     * @param redirectAttributes The attributes used to redirect and show success/error messages.
     * @return Redirects to the permissions page with a success or error message.
     */
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
        return "redirect:/admin/permissions"; // Redirect back to the permissions page
    }

    /**
     * Approves a role/department permission request.
     *
     * @param requestId The ID of the request to approve.
     * @param redirectAttributes The attributes used to redirect and show success/error messages.
     * @return Redirects to the permissions page with a success or error message.
     */
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
        return "redirect:/admin/permissions"; // Redirect back to the permissions page
    }

    /**
     * Denies a role/department permission request.
     *
     * @param requestId The ID of the request to deny.
     * @param redirectAttributes The attributes used to redirect and show success/error messages.
     * @return Redirects to the permissions page with a success or error message.
     */
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
        return "redirect:/admin/permissions"; // Redirect back to the permissions page
    }
}
