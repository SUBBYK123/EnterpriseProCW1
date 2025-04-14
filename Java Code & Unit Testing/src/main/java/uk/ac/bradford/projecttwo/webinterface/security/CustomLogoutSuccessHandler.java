package uk.ac.bradford.projecttwo.webinterface.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import uk.ac.bradford.projecttwo.webinterface.services.LogService;

import java.io.IOException;

/**
 * Custom logout success handler that is triggered when a user logs out successfully.
 * It logs the logout event and redirects the user to the login page with a logout flag.
 */
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final LogService logService;

    /**
     * Constructs a new CustomLogoutSuccessHandler with the given log service.
     *
     * @param logService The service used to log user activity.
     */
    public CustomLogoutSuccessHandler(LogService logService) {
        this.logService = logService;
    }

    /**
     * Handles the logout success event. Logs the logout action to the system and
     * redirects the user to the login page.
     *
     * @param request        The HTTP request.
     * @param response       The HTTP response.
     * @param authentication The authentication object (may be null).
     * @throws IOException      If an input or output error occurs.
     * @throws ServletException If a servlet-specific error occurs.
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {

        if (authentication != null) {
            String email = authentication.getName();
            logService.log(email, "LOGOUT", "SUCCESS");
        }

        // Redirect to login page with logout success query param
        response.sendRedirect("/login?logout=true");
    }
}
