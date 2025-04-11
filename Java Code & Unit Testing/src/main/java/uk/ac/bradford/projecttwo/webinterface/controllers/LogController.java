package uk.ac.bradford.projecttwo.webinterface.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/admin/logs")
    public String showLogs(Model model) {
        model.addAttribute("logs", logService.getLogs());
        return "admin/logs"; // logs.html
    }

    @GetMapping("/admin/logs/search")
    public String filterLogs(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String status,
            Model model
    ) {
        List<LogModel> filteredLogs = logService.filterLogs(email, action, status);
        model.addAttribute("logs", filteredLogs);
        return "admin/logs";
    }


}
