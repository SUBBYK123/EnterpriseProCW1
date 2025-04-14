package uk.ac.bradford.projecttwo.webinterface.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import uk.ac.bradford.projecttwo.webinterface.services.LogService;

import java.io.IOException;
import java.util.Collection;

/**
 * Custom login success handler that redirects users based on their roles after successful authentication.
 * Also provides a point to log login success or trigger other actions post-login.
 */
@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final LogService logService;

    /**
     * Constructor for injecting the LogService.
     *
     * @param logService A logging service to optionally record login events.
     */
    public CustomLoginSuccessHandler(LogService logService) {
        this.logService = logService;
    }

    /**
     * Called when a user has been successfully authenticated.
     * Redirects the user based on their assigned role:
     * - "ADMIN" → /admin/dashboard
     * - "USER"  → /user/home
     * If no known role is matched, redirects to /index by default.
     *
     * @param request        The HTTP request.
     * @param response       The HTTP response.
     * @param authentication The authentication object containing user details.
     * @throws IOException      If an input or output exception occurs.
     * @throws ServletException If a servlet-related error occurs.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ADMIN")) {
                response.sendRedirect("/admin/dashboard");
                return;
            } else if (authority.getAuthority().equals("USER")) {
                response.sendRedirect("/user/home");
                return;
            }
        }

        // default fallback redirection if role is unknown
        response.sendRedirect("/index");
    }

}
