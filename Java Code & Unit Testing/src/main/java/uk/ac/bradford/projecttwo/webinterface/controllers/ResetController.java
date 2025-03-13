package uk.ac.bradford.projecttwo.webinterface.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uk.ac.bradford.projecttwo.webinterface.services.ResetPasswordService;

/**
 * Controller class for handling password reset requests, OTP verification,
 * and password resetting functionality.
 */
@Controller
public class ResetController {

    @Autowired
    private ResetPasswordService resetPasswordService;

    /**
     * Serves the Reset Password Request page.
     * @return The name of the reset password request template.
     */
    @GetMapping("/reset")
    public String showResetPage() {
        return "reset"; // Make sure reset.html exists in src/main/resources/templates/
    }

    /**
     * Serves the OTP Verification page.
     * @return The name of the OTP verification template.
     */
    @GetMapping("/reset_verify")
    public String showVerifyPage() {
        return "redirect:/reset_verify"; // Make sure reset_verify.html exists in src/main/resources/templates/
    }

    /**
     * Handles OTP request submission and sends an OTP to the provided email address.
     * @param emailAddress The email address to which the OTP should be sent.
     * @param model The model to add success or error messages.
     * @return The name of the template to be rendered next.
     */
    @PostMapping("/request")
    public String requestOtp(@RequestParam("emailAddress") String emailAddress, Model model) {
        try {
            resetPasswordService.generateAndSendOtp(emailAddress);
            model.addAttribute("successMessage", "OTP sent to your email.");
            return "redirect:/reset_verify"; // Redirect to OTP verification page
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "reset"; // Show the same page with an error message
        }
    }

    /**
     * Handles OTP verification submission.
     * @param email The email address associated with the OTP.
     * @param otp The OTP to be verified.
     * @param model The model to add success or error messages.
     * @return The name of the template to be rendered next.
     */
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

    /**
     * Handles password reset submission.
     * @param email The email address for which the password is being reset.
     * @param otp The OTP required for authentication.
     * @param newPassword The new password to be set.
     * @param model The model to add success or error messages.
     * @return The name of the template to be rendered next.
     */
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
