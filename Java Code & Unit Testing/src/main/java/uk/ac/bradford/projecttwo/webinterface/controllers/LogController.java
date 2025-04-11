package uk.ac.bradford.projecttwo.webinterface.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uk.ac.bradford.projecttwo.webinterface.models.LogModel;
import uk.ac.bradford.projecttwo.webinterface.services.LogService;

import java.util.List;

/**
 * Controller for managing log-related functionalities in the admin section.
 * This class handles the display and filtering of logs for administrators.
 */
@Controller
public class LogController {

    // Service for fetching and filtering log data
    private final LogService logService;

    /**
     * Constructor to initialize the LogController with the required LogService.
     *
     * @param logService The service used to fetch and filter logs.
     */
    @Autowired
    public LogController(LogService logService) {
        this.logService = logService;
    }

    /**
     * Displays all logs to the admin.
     *
     * @param model The model object to be passed to the view.
     * @return The name of the view template ("admin/logs") to display the logs.
     */
    @GetMapping("/admin/logs")
    public String showLogs(Model model) {
        model.addAttribute("logs", logService.getLogs()); // Add the logs to the model
        return "admin/logs"; // Render the logs.html page
    }

    /**
     * Filters the logs based on given parameters (email, action, status).
     *
     * @param email The email to filter logs by (optional).
     * @param action The action to filter logs by (optional).
     * @param status The status to filter logs by (optional).
     * @param model The model object to be passed to the view.
     * @return The name of the view template ("admin/logs") to display the filtered logs.
     */
    @GetMapping("/admin/logs/search")
    public String filterLogs(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String status,
            Model model
    ) {
        // Filter logs based on the provided parameters
        List<LogModel> filteredLogs = logService.filterLogs(email, action, status);
        model.addAttribute("logs", filteredLogs); // Add the filtered logs to the model
        return "admin/logs"; // Render the logs.html page with filtered logs
    }
}
