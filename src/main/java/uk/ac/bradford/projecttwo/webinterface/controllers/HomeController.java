package uk.ac.bradford.projecttwo.webinterface.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling home-related requests.
 * Provides an endpoint to greet authenticated users with their roles.
 */
@RestController
public class HomeController {

    /**
     * Returns a greeting message for the authenticated user.
     *
     * @param authentication The Authentication object containing user details.
     * @return A personalized greeting message with the user's role.
     */
    @GetMapping("/hello")
    public String hello(Authentication authentication) {
        // Get user details from authentication object
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Retrieve the first authority (role) of the user
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(Object::toString)
                .orElse("USER"); // Default to "USER" if no role is found

        // Return a personalized greeting message
        return "Hello " + userDetails.getUsername() + ". You are a " + role + " on this project.";
    }
}
