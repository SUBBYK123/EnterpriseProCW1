package uk.ac.bradford.projecttwo.webinterface.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import uk.ac.bradford.projecttwo.webinterface.models.RegistrationModel;

@Controller
public class UserRegistrationController {

    @GetMapping("/registration")
    public String showSignUpForm(Model model){
        model.addAttribute("registerUser",new RegistrationModel());
        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute RegistrationModel user){
        System.out.println("User Registered: " + user.getFirstName() + " " + user.getLastName());
        return "redirect:/signup?success";
    }


}
