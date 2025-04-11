package uk.ac.bradford.projecttwo.webinterface.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller class responsible for handling the admin-related routes.
 * It defines the logic for the admin dashboard page.
 */
@Controller
public class AdminController {

    /**
     * Handles the GET request to the admin dashboard.
     * It returns the view name for the admin dashboard page.
     *
     * @return The view name for the admin dashboard page.
     */
    @GetMapping("/admin/dashboard")
    public String showAdminDashboard() {
        return "admin/admin_dashboard";
    }

}
