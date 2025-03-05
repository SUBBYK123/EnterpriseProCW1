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


@Controller
public class LoginController {
    private Logger logger = LoggerFactory.getLogger(LoginController.class);
    private final LoginServiceImpl loginService;

    public LoginController(LoginServiceImpl loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, Model model) {

        if (error != null){
            model.addAttribute("errorMessage","Invalid Email or Password");
        }

        return "login";  // This returns login.html from the templates folder
    }

    @PostMapping("/login")
    public String loginForm(@ModelAttribute LoginModel loginModel, Model model, HttpSession session){
        String email = loginModel.getEmailAddress();
        String password = loginModel.getPassword();

        if (loginService.authenticateUser(email,password)){
            session.setAttribute("user",email);
            logger.info("User {} logged in successfully",email);
            return "redirect:/index";
        }
        else{
            model.addAttribute("errorMessage","Invalid Email or Password");
            return "redirect:/login?error";
        }

    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();  // Destroy session
        logger.info("User logged out.");
        return "redirect:/login";
    }


}
