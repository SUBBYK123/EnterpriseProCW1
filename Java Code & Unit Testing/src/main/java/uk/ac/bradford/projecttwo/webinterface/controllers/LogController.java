package uk.ac.bradford.projecttwo.webinterface.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import uk.ac.bradford.projecttwo.webinterface.models.LogModel;
import uk.ac.bradford.projecttwo.webinterface.services.LogService;

import java.util.List;

@Controller
public class LogController {
    private final LogService logService;

    @Autowired
    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping("/logs")
    public String showLogs(Model model) {
        model.addAttribute("logs", logService.getLogs());
        return "logs"; // logs.html
    }
}
