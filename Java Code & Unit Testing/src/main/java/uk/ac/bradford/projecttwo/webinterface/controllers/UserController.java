package uk.ac.bradford.projecttwo.webinterface.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/user/dashboard")
    public String showUserDashboard() {
        return "user/user_dashboard";
    }

    @GetMapping("/user/home")
    public String showUserHome() {
        return "user/user_home";
    }


    @GetMapping("/user/search")
    public String showSearchPage() {
        return "user/search";
    }


    @GetMapping("/user/2fa")
    public String show2FA() {
        return "user/2FA";
    }

    @GetMapping("/user/2fa-verify")
    public String show2FAVerify() {
        return "user/2FA_verify";
    }
}
