package uk.ac.bradford.projecttwo.webinterface.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.bradford.projecttwo.webinterface.services.LogService;
import uk.ac.bradford.projecttwo.webinterface.models.Log;
import uk.ac.bradford.projecttwo.webinterface.repositories.UserRepository;
import uk.ac.bradford.projecttwo.webinterface.repositories.LogRepository;
import java.time.LocalDateTime;

@Controller
public class LogController {

    private final LogService logService;
    private final LogRepository logRepository;
    private final UserRepository userRepository;

    // Constructor injection for all dependencies
    @Autowired
    public LogController(LogService logService, LogRepository logRepository, UserRepository userRepository) {
        this.logService = logService;
        this.logRepository = logRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/logs")
    public String showLogs(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // Log the access to the logs page
        Log log = new Log();
        log.setUser(UserRepository.findByEmail(userDetails.getUsername())); // Get the current user
        log.setActionMessage("Accessed the logs page");
        log.setTimestamp(LocalDateTime.now());
        logRepository.save(log); // Save the log to the database

        // Add logs to the model for display
        model.addAttribute("logs", logService.getAllLogs());
        return "logs";
    }
}