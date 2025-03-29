package uk.ac.bradford.projecttwo.webinterface.controllers;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uk.ac.bradford.projecttwo.webinterface.models.LoginModel;
import uk.ac.bradford.projecttwo.webinterface.services.LogService;
import uk.ac.bradford.projecttwo.webinterface.services.LogServiceImpl;
import uk.ac.bradford.projecttwo.webinterface.services.LoginServiceImpl;

/**
 * Controller for handling user login and logout functionality.
 * This controller manages login requests, authentication, and session handling.
 */
@Controller
public class LoginController {

    // Logger for tracking login and logout events
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private final LogServiceImpl logService;
    // Service for handling authentication logic
    private final LoginServiceImpl loginService;

    /**
     * Constructor-based dependency injection for LoginServiceImpl.
     *
     * @param logService
     * @param loginService The service responsible for user authentication.
     */
    @Autowired
    public LoginController(LogServiceImpl logService, LoginServiceImpl loginService) {
        this.logService = logService;
        this.loginService = loginService;
    }

    /**
     * Displays the login page.
     * If an error parameter is present, it adds an error message to the model.
     *
     * @param error Optional request parameter indicating a failed login attempt.
     * @param model The Model object to pass attributes to the view.
     * @return The name of the login page template.
     */
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid Email or Password");
        }
        return "login"; // This returns login.html from the templates folder
    }

}
