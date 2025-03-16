package uk.ac.bradford.projecttwo.webinterface.controllers;

// Spring Framework Imports
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Permission-related Imports
import uk.ac.bradford.projecttwo.webinterface.services.PermissionService;
import uk.ac.bradford.projecttwo.webinterface.models.PermissionRequest;
import uk.ac.bradford.projecttwo.webinterface.models.PermissionRequest.ApprovalStatus;

// Java Imports
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/permissions")
public class PermissionsController {

    private final PermissionService permissionService;

    @Autowired
    public PermissionsController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    public String showPermissions(Model model) {
        model.addAttribute("requests", permissionService.getAllRequests());
        return "permissions";
    }

    @PostMapping("/approve/{requestId}")
    public String approveRequest(@PathVariable Integer requestId, RedirectAttributes redirectAttributes) {
        try {
            permissionService.updateRequestStatus(requestId, ApprovalStatus.APPROVED);
            redirectAttributes.addFlashAttribute("success", "Request approved successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error approving request: " + e.getMessage());
        }
        return "redirect:/permissions";
    }

    @PostMapping("/deny/{requestId}")
    public String denyRequest(@PathVariable Integer requestId, RedirectAttributes redirectAttributes) {
        try {
            permissionService.updateRequestStatus(requestId, ApprovalStatus.DENIED);
            redirectAttributes.addFlashAttribute("success", "Request denied successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error denying request: " + e.getMessage());
        }
        return "redirect:/permissions";
    }
}