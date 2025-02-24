package uk.ac.bradford.projecttwo.webinterface.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import uk.ac.bradford.projecttwo.webinterface.models.RegistrationModel;

@Controller
public class AdminController {
    @GetMapping("/admin/dashboard")
    public String index(Model model, Authentication authentication) {
        String username = authentication.getName();
        model.addAttribute("username", username );
        return "/admin/dashboard";
    }

}
