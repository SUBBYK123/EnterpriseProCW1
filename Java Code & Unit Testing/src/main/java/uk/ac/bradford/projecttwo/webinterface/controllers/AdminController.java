package uk.ac.bradford.projecttwo.webinterface.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import uk.ac.bradford.projecttwo.webinterface.models.RegistrationModel;

/**
 * Controller for handling admin-related requests.
 * This controller manages access to the admin dashboard.
 */
@Controller
public class AdminController {

    /**
     * Handles requests to the admin dashboard.
     *
     * @param model The Model object used to pass attributes to the view.
     * @param authentication The Authentication object containing user authentication details.
     * @return The path to the admin dashboard template.
     */
    @GetMapping("/admin/dashboard")
    public String index(Model model, Authentication authentication) {
        // Retrieve the currently authenticated username (typically an email)
        String username = authentication.getName();

        // Add the username as a model attribute to display it in the dashboard
        model.addAttribute("username", username);

        // Return the path to the admin dashboard template
        return "/admin/dashboard";
    }
}
