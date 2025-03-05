package uk.ac.bradford.projecttwo.webinterface.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import uk.ac.bradford.projecttwo.webinterface.models.ResetPasswordModel;

@Controller
public class ResetController {

    // Basic password reset flow example
    @GetMapping("/reset")
    public String reset(Model model) {
        model.addAttribute("resetRequest", new ResetPasswordModel());
        return "reset"; // Ensure this matches your template name
    }

    @GetMapping("/reset_verify")
    public String reset_verify(Model model) {
        model.addAttribute("resetRequest", new ResetPasswordModel());
        return "reset_verify"; // Ensure this matches your template name
    }

//    @PostMapping("/reset")
//    public String processResetRequest(@ModelAttribute("resetRequest") ResetPasswordModel resetRequest, Model model) {
//        // Add validation logic here
//        if (resetRequest.getEmailAddress() == null || resetRequest.getEmailAddress().isEmpty()) {
//            model.addAttribute("error", "Email is required");
//            return "reset";
//        }
//
//        model.addAttribute("message", "Password reset instructions sent to your email");
//        return "reset";
//    }
}