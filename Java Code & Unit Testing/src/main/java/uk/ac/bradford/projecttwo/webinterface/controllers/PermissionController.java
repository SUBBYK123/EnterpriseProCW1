package uk.ac.bradford.projecttwo.webinterface.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uk.ac.bradford.projecttwo.webinterface.services.LogService;
import uk.ac.bradford.projecttwo.webinterface.services.PermissionRequestService;

@Controller
@RequestMapping("/permissions")
public class PermissionController {


    private final PermissionRequestService permissionRequestService;
    private final LogService logService;

    @Autowired
    public PermissionController(PermissionRequestService permissionRequestService, LogService logService) {
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
    public String approveRequest(@PathVariable("id") int id) {
        boolean result = permissionRequestService.approveRequest(id);

        if (result) {
            return "redirect:/permissions?success=Permission approved successfully.";

        } else {
            return "redirect:/permissions?error=Failed to approve the request.";
        }
    }

    /**
     * Handles denial of a permission request by ID.
     */
    @PostMapping("/deny/{id}")
    public String denyRequest(@PathVariable("id") int id) {
        boolean result = permissionRequestService.denyRequest(id);

        if (result) {
            return "redirect:/permissions?success=Permission denied successfully.";
        } else {
            return "redirect:/permissions?error=Failed to deny the request.";
        }
    }

}
