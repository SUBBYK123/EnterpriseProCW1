package uk.ac.bradford.projecttwo.webinterface.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    @GetMapping("/admin/dashboard")
    public String index(Model model, Authentication authentication) {
        String username = authentication.getName();
        model.addAttribute("username", username );
        return "/admin/dashboard";
    }
}
