package uk.ac.bradford.projecttwo.webinterface.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import uk.ac.bradford.projecttwo.webinterface.models.DatasetAccessRequestModel;
import uk.ac.bradford.projecttwo.webinterface.services.DatasetAccessRequestService;

import java.security.Principal;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    DatasetAccessRequestService datasetAccessRequestService;

    @GetMapping("/user/dashboard")
    public String showUserDashboard() {
        return "user/user_dashboard";
    }

    @GetMapping("/user/home")
    public String showUserHome() {
        return "user/user_home";
    }


    @GetMapping("/user/my-requests")
    public String showUserRequests(Model model, Principal principal) {
        String email = principal.getName();
        List<DatasetAccessRequestModel> requests = datasetAccessRequestService.getRequestsByEmail(email);
        model.addAttribute("requests", requests);
        return "user/requests";
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
