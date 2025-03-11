package uk.ac.bradford.projecttwo.webinterface.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.bradford.projecttwo.webinterface.repositories.UserRepository;
import uk.ac.bradford.projecttwo.webinterface.models.User;

@Controller
@RequestMapping("/user-access")
public class UserAccessController {
    private final UserRepository userRepository;

    @Autowired
    public UserAccessController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String userAccessControl(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "user-access";
    }

    @PostMapping("/approve/{userId}")
    public String approveUser(@PathVariable Integer userId, RedirectAttributes redirectAttributes) {
        try {
            User user = (User) userRepository.findById(userId);
            if (user == null) {
                throw new RuntimeException("User not found");
            }
            user.setStatus(User.AccountStatus.APPROVED);
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("successMessage", "User approved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error approving user: " + e.getMessage());
        }
        return "redirect:/user-access";
    }

    @PostMapping("/deny/{userId}")
    public String denyUser(@PathVariable Integer userId, RedirectAttributes redirectAttributes) {
        try {
            User user = (User) userRepository.findById(userId);
            if (user == null) {
                throw new RuntimeException("User not found");
            }
            user.setStatus(User.AccountStatus.DENIED);
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("successMessage", "User denied successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error denying user: " + e.getMessage());
        }
        return "redirect:/user-access";
    }

}
