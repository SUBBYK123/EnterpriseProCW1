package uk.ac.bradford.projecttwo.webinterface.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import uk.ac.bradford.projecttwo.webinterface.models.DatasetAccessRequestModel;
import uk.ac.bradford.projecttwo.webinterface.services.DatasetAccessRequestService;

import java.security.Principal;
import java.util.List;

/**
 * Controller for handling user-specific actions such as viewing their dashboard,
 * home page, dataset access requests, and two-factor authentication (2FA) flows.
 */
@Controller
public class UserController {

    // Service for managing dataset access requests
    @Autowired
    DatasetAccessRequestService datasetAccessRequestService;

    /**
     * Displays the user dashboard page.
     *
     * @return The name of the view to render ("user/user_dashboard").
     */
    @GetMapping("/user/dashboard")
    public String showUserDashboard() {
        return "user/user_dashboard";
    }

    /**
     * Displays the user home page.
     *
     * @return The name of the view to render ("user/user_home").
     */
    @GetMapping("/user/home")
    public String showUserHome() {
        return "user/user_home";
    }

    /**
     * Displays the user's dataset access requests.
     * Fetches requests based on the logged-in user's email.
     *
     * @param model The model to pass data to the view.
     * @param principal The current authenticated user.
     * @return The name of the view to render ("user/requests").
     */
    @GetMapping("/user/my-requests")
    public String showUserRequests(Model model, Principal principal) {
        String email = principal.getName(); // Get the email address of the logged-in user
        List<DatasetAccessRequestModel> requests = datasetAccessRequestService.getRequestsByEmail(email);
        model.addAttribute("requests", requests); // Add the user's requests to the model
        return "user/requests"; // Render the requests page
    }

    /**
     * Displays the search page for users to search datasets.
     *
     * @return The name of the view to render ("user/search").
     */
    @GetMapping("/user/search")
    public String showSearchPage() {
        return "user/search";
    }

    /**
     * Displays the 2FA (Two-Factor Authentication) page for the user.
     *
     * @return The name of the view to render ("user/2FA").
     */
    @GetMapping("/user/2fa")
    public String show2FA() {
        return "user/2FA";
    }

    /**
     * Displays the 2FA verification page for the user.
     *
     * @return The name of the view to render ("user/2FA_verify").
     */
    @GetMapping("/user/2fa-verify")
    public String show2FAVerify() {
        return "user/2FA_verify";
    }
}
