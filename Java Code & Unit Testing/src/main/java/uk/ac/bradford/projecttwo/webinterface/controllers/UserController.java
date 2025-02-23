package uk.ac.bradford.projecttwo.webinterface.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import uk.ac.bradford.projecttwo.webinterface.models.RegistrationModel;

@Controller
public class UserController {

    @GetMapping("/signup")
    public String showSignupForm(Model model){
        model.addAttribute("user", new RegistrationModel());
        return "signup";
    }


}
