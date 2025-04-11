package uk.ac.bradford.projecttwo.webinterface.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller that handles general routing for various pages such as contact, login, signup, and reset pages.
 * These routes are for user-facing pages that provide functionality like login, signup, contact forms, and password reset.
 */
@Controller
public class GeneralController {

    /**
     * Displays the contact us page.
     *
     * @return The name of the view (contactus) that displays the contact page.
     */
    @GetMapping("/contact")
    public String showContactPage() {
        return "contactus";
    }

    /**
     * Displays the login page for users.
     *
     * @return The name of the view (login) that displays the login form.
     */
    @GetMapping("/user-login")
    public String showLoginPage() {
        return "login";
    }

    /**
     * Displays the signup page for new users.
     *
     * @return The name of the view (signup) that displays the signup form.
     */
    @GetMapping("/user-signup")
    public String showSignupPage() {
        return "signup";
    }

    /**
     * Displays the page indicating that the user's signup is pending approval.
     *
     * @return The name of the view (signup_pending) that informs the user about the pending signup.
     */
    @GetMapping("/user-signup-pending")
    public String showSignupPendingPage() {
        return "signup_pending";
    }

    /**
     * Displays the password reset page where users can initiate a password reset.
     *
     * @return The name of the view (reset) that shows the password reset form.
     */
    @GetMapping("/user-reset")
    public String showResetPage() {
        return "reset";
    }

    /**
     * Displays the page where users verify their OTP for password reset.
     *
     * @return The name of the view (reset_verify) that allows users to verify their OTP.
     */
    @GetMapping("/user-reset_verify")
    public String showResetVerifyPage() {
        return "reset_verify";
    }
}
