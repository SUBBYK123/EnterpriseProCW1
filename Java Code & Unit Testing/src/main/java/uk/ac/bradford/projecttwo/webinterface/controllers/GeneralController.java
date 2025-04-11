package uk.ac.bradford.projecttwo.webinterface.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GeneralController {

    @GetMapping("/contact")
    public String showContactPage() {
        return "contactus";
    }

    @GetMapping("/user-login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/user-signup")
    public String showSignupPage() {
        return "signup";
    }

    @GetMapping("/user-signup-pending")
    public String showSignupPendingPage() {
        return "signup_pending";
    }

    @GetMapping("/user-reset")
    public String showResetPage() {
        return "reset";
    }

    @GetMapping("/user-reset_verify")
    public String showResetVerifyPage() {
        return "reset_verify";
    }
}
