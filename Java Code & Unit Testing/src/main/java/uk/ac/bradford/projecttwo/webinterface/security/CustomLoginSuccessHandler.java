package uk.ac.bradford.projecttwo.webinterface.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import uk.ac.bradford.projecttwo.webinterface.services.EmailService;
import uk.ac.bradford.projecttwo.webinterface.services.LogService;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final LogService logService;
    private final EmailService emailService;

    public CustomLoginSuccessHandler(LogService logService, EmailService emailService) {
        this.logService = logService;
        this.emailService = emailService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String email = authentication.getName(); // get email/username
        logService.log(email, "LOGIN", "SUCCESS");

        // ✅ Generate and send OTP
        String otp = OTPUtil.generateOTP();
        HttpSession session = request.getSession();
        session.setAttribute("otp", otp);
        session.setAttribute("otpEmail", email);

        try {
            emailService.sendLoginOtpEmail(email, otp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ✅ Redirect user to OTP verification page
        response.sendRedirect("/verify-otp");
    }
}
