package uk.ac.bradford.projecttwo.webinterface.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.ac.bradford.projecttwo.webinterface.services.LogService;
import uk.ac.bradford.projecttwo.webinterface.services.LogServiceImpl;
import uk.ac.bradford.projecttwo.webinterface.services.PermissionRequestService;

@Controller
@RequestMapping("/permissions")
public class PermissionController {


    private final PermissionRequestService permissionRequestService;
    private final LogServiceImpl logService;

    @Autowired
    public PermissionController(PermissionRequestService permissionRequestService, LogServiceImpl logService) {
        this.permissionRequestService = permissionRequestService;
        this.logService = logService;
    }

    /**
     * Displays all pending and reviewed permission requests.
     */
    @GetMapping
    public String showPermissionRequests(Model model,
                                         @RequestParam(value = "success", required = false) String success,
                                         @RequestParam(value = "error", required = false) String error) {

        model.addAttribute("requests", permissionRequestService.getAllRequests());

        if (success != null) model.addAttribute("success", success);
        if (error != null) model.addAttribute("error", error);

        return "permissions"; // Thymeleaf file: permissions.html
    }

    /**
     * Handles approval of a permission request by ID.
     */
    @PostMapping("/approve/{id}")
    public String approveRequest(@PathVariable("id") int requestId, RedirectAttributes redirectAttributes) {
        try {
            permissionRequestService.approveRequest(requestId);
            String email = permissionRequestService.getRequestEmailById(requestId);
            logService.log(email, "APPROVE", "SUCCESS");
            redirectAttributes.addFlashAttribute("success", "Request approved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error approving request.");
        }
        return "redirect:/permissions";
    }


    /**
     * Handles denial of a permission request by ID.
     */
    @PostMapping("/deny/{id}")
    public String denyRequest(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            permissionRequestService.denyRequest(id);
            String email = permissionRequestService.getRequestEmailById(id);
            logService.log(email, "DENY", "SUCCESS");
            redirectAttributes.addFlashAttribute("error", "Request Denied.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error approving request.");
        }
        return "redirect:/permissions";
    }

}
