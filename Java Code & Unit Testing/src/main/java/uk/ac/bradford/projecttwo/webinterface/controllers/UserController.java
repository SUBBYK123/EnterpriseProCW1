package uk.ac.bradford.projecttwo.webinterface.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import uk.ac.bradford.projecttwo.webinterface.models.RegistrationModel;
import uk.ac.bradford.projecttwo.webinterface.repositories.RegistrationRepositoryImpl;

import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    RegistrationRepositoryImpl registrationRepository;

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new RegistrationModel());
        return "signup";
    }

    @PostMapping("/signup")
    public String processSignup(@ModelAttribute("user") RegistrationModel user, Model model) {

        try {
            Optional<RegistrationModel> existingUser = Optional.ofNullable(registrationRepository.findUserByEmail(user.getEmailAddress()));

            if (existingUser.isPresent()) {
                model.addAttribute("errorMessage", "Email Already Taken!");
                return "signup";
            } else {
                registrationRepository.registerUser(user);
                return "redirect:/login?signupsuccess";
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "signup";

        }
    }

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("user", new RegistrationModel());
        return "index";
    }


}
