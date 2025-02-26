package uk.ac.bradford.projecttwo.webinterface.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import uk.ac.bradford.projecttwo.webinterface.models.LoginModel;


@Controller
public class LoginController {
    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @GetMapping("/login")
    public String login(Model model) {
        return "login";  // This returns login.html from the templates folder
    }

    @PostMapping("/login")
    public String loginForm(@ModelAttribute LoginModel loginModel,Model model){
        return "redirect:/login?loginsuccess";

    }


}
