package uk.ac.bradford.projecttwo.webinterface.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uk.ac.bradford.projecttwo.webinterface.models.ResetPasswordModel;
import uk.ac.bradford.projecttwo.webinterface.services.ResetPasswordService;

import java.util.Map;

@Controller
public class ResetController {

    @Autowired
    private ResetPasswordService resetPasswordService;

    // Serve the Reset Password Request Page
    @GetMapping("/reset")
    public String showResetPage() {
        return "reset"; // Make sure reset.html exists in src/main/resources/templates/
    }

    // Serve the OTP Verification Page
    @GetMapping("/reset_verify")
    public String showVerifyPage() {
        return "reset_verify"; // Make sure reset_verify.html exists in src/main/resources/templates/
    }

    // Handle OTP Request Submission
    @PostMapping("/request")
    public String requestOtp(@RequestParam("emailAddress") String emailAddress, Model model) {
        try {
            resetPasswordService.generateAndSendOtp(emailAddress);
            model.addAttribute("successMessage", "OTP sent to your email.");
            return "reset_verify"; // Redirect to OTP verification page
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "reset"; // Show the same page with an error message
        }
    }

    // Handle OTP Verification Submission
    @PostMapping("/verify")
    public String verifyOtp(@RequestParam("emailAddress") String email, @RequestParam("otp") String otp, Model model) {
        if (resetPasswordService.verifyOtp(email, otp)) {
            model.addAttribute("email", email);
            model.addAttribute("otp", otp);
            return "reset_password"; // Forward to the password reset page
        } else {
            model.addAttribute("errorMessage", "Invalid or expired OTP.");
            return "reset_verify"; // Reload the OTP verification page with an error
        }
    }

    // Handle Password Reset Submission
    @PostMapping("/reset")
    public String resetPassword(@RequestParam("emailAddress") String email,
                                @RequestParam("otp") String otp,
                                @RequestParam("newPassword") String newPassword,
                                Model model) {
        if (resetPasswordService.resetPassword(email, otp, newPassword)) {
            model.addAttribute("successMessage", "Password reset successfully. You can now log in.");
            return "login"; // Redirect to the login page
        } else {
            model.addAttribute("errorMessage", "Invalid OTP or expired.");
            return "reset_password"; // Reload password reset page with an error
        }
    }
}
