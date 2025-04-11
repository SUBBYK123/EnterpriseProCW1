package uk.ac.bradford.projecttwo.webinterface.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import uk.ac.bradford.projecttwo.webinterface.models.RegistrationModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.RegistrationRepositoryImpl;
import uk.ac.bradford.projecttwo.webinterface.services.LogServiceImpl;
import uk.ac.bradford.projecttwo.webinterface.services.RegistrationService;

import java.util.Optional;

/**
 * Controller for handling user signup.
 * This controller manages user registration and redirects to the login page upon successful signup.
 */
@Controller
public class SignupController {

    // Injects the RegistrationRepository for handling database operations
    @Autowired
    private RegistrationRepositoryImpl registrationRepository;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private LogServiceImpl logService;

    /**
     * Displays the signup form.
     *
     * @param model The Model object used to pass attributes to the view.
     * @return The name of the signup page template.
     */
    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new RegistrationModel());
        return "signup"; // Returns signup.html from the templates folder
    }



    /**
     * Processes the user signup request.
     * Checks if the email is already taken, and if not, registers the new user.
     *
     * @param user  The RegistrationModel object containing user details.
     * @param model The Model object used to pass attributes to the view.
     * @return Redirects to the login page if successful, otherwise reloads the signup page with an error message.
     */
    @PostMapping("/signup")
    public String processSignup(@ModelAttribute("user") RegistrationModel user, Model model) {
        try {
            // Check if the user with the provided email already exists
            Optional<RegistrationModel> existingUser = Optional.ofNullable(registrationRepository.findUserByEmail(user.getEmailAddress()));

            if (existingUser.isPresent()) {
                logService.log(user.getEmailAddress(), "REGISTER","FAILED");
                model.addAttribute("errorMessage", "Email Already Taken!");
                return "signup"; // Reloads the signup page with an error message
            } else {
                registrationService.registerUser(user);
                logService.log(user.getEmailAddress(), "REGISTER","SUCCESS");
                return "redirect:/signup/pending";
            }
        } catch (Exception e) {
            logService.log(user.getEmailAddress(),"REGISTER","ERROR");
            model.addAttribute("errorMessage", "An error occurred: " + e.getMessage());
            return "signup"; // Reloads signup page with the error message
        }
    }

    @GetMapping("/signup/pending")
    public String showPendingPage() {
        return "signup_pending";
    }


    /**
     * Displays the index page.
     *
     * @param model The Model object used to pass attributes to the view.
     * @return The name of the index page template.
     */
    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("user", new RegistrationModel());
        return "index"; // Returns user_home.html from the templates folder
    }
}
