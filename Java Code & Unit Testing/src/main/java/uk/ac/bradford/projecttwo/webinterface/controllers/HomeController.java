package uk.ac.bradford.projecttwo.webinterface.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class HomeController {

    @GetMapping("/hello")
    public String hello(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return "Hello " + userDetails.getUsername() + ". You are a " + userDetails.getAuthorities().stream().findFirst() + " on this project.";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup"; // Refers to src/main/resources/templates/signup.html
    }

}
