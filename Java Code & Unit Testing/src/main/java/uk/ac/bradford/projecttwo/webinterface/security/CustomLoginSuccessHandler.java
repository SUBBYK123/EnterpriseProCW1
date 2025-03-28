package uk.ac.bradford.projecttwo.webinterface.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import uk.ac.bradford.projecttwo.webinterface.services.LogService;

import java.io.IOException;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final LogService logService;

    public CustomLoginSuccessHandler(LogService logService) {
        this.logService = logService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String email = authentication.getName();
        logService.log(email, "LOGIN", "SUCCESS");

        response.sendRedirect("/index");
    }
}
