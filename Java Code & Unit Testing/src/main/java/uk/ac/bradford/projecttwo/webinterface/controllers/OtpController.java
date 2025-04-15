package uk.ac.bradford.projecttwo.webinterface.controllers;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.ac.bradford.projecttwo.webinterface.models.LoginModel;
import uk.ac.bradford.projecttwo.webinterface.security.OTPUtil;
import uk.ac.bradford.projecttwo.webinterface.services.EmailService;
import uk.ac.bradford.projecttwo.webinterface.services.LoginServiceImpl;

import java.io.IOException;

/**
 * Controller for handling OTP verification during login (2FA).
 */
@Controller
public class OtpController {

    private final LoginServiceImpl loginService;
    private final EmailService emailService;

    @Autowired
    public OtpController(LoginServiceImpl loginService, EmailService emailService) {
        this.loginService = loginService;
        this.emailService = emailService;
    }

    /**
     * Displays the OTP verification page.
     */
    @GetMapping("/verify-otp")
    public String showOtpPage(HttpSession session, Model model) {
        if (session.getAttribute("otpEmail") == null) {
            model.addAttribute("errorMessage", "Session expired. Please log in again.");
            return "redirect:/login";
        }
        return "2FA";
    }

    /**
     * Verifies the submitted OTP and redirects based on user role.
     */
    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String otp, HttpSession session, Model model) {
        String storedOtp = (String) session.getAttribute("otp");
        String email = (String) session.getAttribute("otpEmail");

        if (storedOtp != null && storedOtp.equals(otp)) {
            LoginModel user = loginService.findUserByEmail(email);
            session.removeAttribute("otp");
            session.removeAttribute("otpEmail");

            if (user != null) {
                return "redirect:" + (user.getRole().equalsIgnoreCase("ADMIN")
                        ? "/admin/dashboard"
                        : "/user/home");
            }
        }

        model.addAttribute("errorMessage", "Invalid OTP. Please try again.");
        return "2FA";
    }

    /**
     * Resends a new OTP to the user email stored in session.
     */
    @PostMapping("/resend-otp")
    public String resendOtp(HttpSession session, RedirectAttributes redirectAttributes) {
        String email = (String) session.getAttribute("otpEmail");

        if (email == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Session expired. Please log in again.");
            return "redirect:/login";
        }

        try {
            String newOtp = OTPUtil.generateOTP();
            session.setAttribute("otp", newOtp);
            emailService.sendLoginOtpEmail(email, newOtp);
            redirectAttributes.addFlashAttribute("success", "A new OTP has been sent to your email.");
        } catch (MessagingException | IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to resend OTP. Please try again.");
        }

        return "redirect:/verify-otp";
    }
}
