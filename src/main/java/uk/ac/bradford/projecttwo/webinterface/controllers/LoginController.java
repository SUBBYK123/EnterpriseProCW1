package uk.ac.bradford.projecttwo.webinterface.controllers;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uk.ac.bradford.projecttwo.webinterface.models.LoginModel;
import uk.ac.bradford.projecttwo.webinterface.services.LoginServiceImpl;

/**
 * Controller for handling user login and logout functionality.
 * This controller manages login requests, authentication, and session handling.
 */
@Controller
public class LoginController {

    // Logger for tracking login and logout events
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    // Service for handling authentication logic
    private final LoginServiceImpl loginService;

    /**
     * Constructor-based dependency injection for LoginServiceImpl.
     *
     * @param loginService The service responsible for user authentication.
     */
    public LoginController(LoginServiceImpl loginService) {
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

    /**
     * Handles login form submissions.
     * Authenticates the user and creates a session if successful.
     *
     * @param loginModel The LoginModel containing email and password.
     * @param model      The Model object to pass attributes to the view.
     * @param session    The HttpSession to store authenticated user details.
     * @return Redirects to the index page if successful, otherwise redirects to
     *         login with an error message.
     */
    @PostMapping("/login")
    public String loginForm(@ModelAttribute LoginModel loginModel, Model model, HttpSession session) {
        String email = loginModel.getEmailAddress();
        String password = loginModel.getPassword();

        if (loginService.authenticateUser(email, password)) {
            session.setAttribute("user", email);
            logger.info("User {} logged in successfully", email);
            return "redirect:/index";
        } else {
            model.addAttribute("errorMessage", "Invalid Email or Password");
            return "redirect:/login?error";
        }
    }

    /**
     * Logs out the user by invalidating the session.
     *
     * @param session The HttpSession object to invalidate.
     * @return Redirects to the login page after logout.
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Destroy the session
        logger.info("User logged out.");
        return "redirect:/login";
    }
}
