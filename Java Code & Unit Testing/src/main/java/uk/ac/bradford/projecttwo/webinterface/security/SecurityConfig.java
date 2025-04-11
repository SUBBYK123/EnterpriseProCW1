package uk.ac.bradford.projecttwo.webinterface.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Security configuration class that defines authentication and authorization
 * settings
 * for the web application using Spring Security.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Dependency on Encryptor class for password hashing (not used directly in this config)
    private final Encryptor encryptor;
    private final CustomLoginSuccessHandler customLoginSuccessHandler;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;

    /**
     * Constructor-based dependency injection for Encryptor.
     *  @param encryptor An instance of Encryptor for password encryption.
     * @param customLoginSuccessHandler
     * @param customLogoutSuccessHandler
     */
    public SecurityConfig(Encryptor encryptor, CustomLoginSuccessHandler customLoginSuccessHandler, CustomLogoutSuccessHandler customLogoutSuccessHandler) {
        this.encryptor = encryptor;
        this.customLoginSuccessHandler = customLoginSuccessHandler;
        this.customLogoutSuccessHandler = customLogoutSuccessHandler;
    }

    /**
     * Configures HTTP security settings such as authentication and authorization.
     *
     * @param http The HttpSecurity object to configure security policies.
     * @return A SecurityFilterChain instance with configured security rules.
     * @throws Exception If an error occurs while configuring security settings.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomLoginSuccessHandler customLoginSuccessHandler,CustomLogoutSuccessHandler customLogoutSuccessHandler) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // Publicly accessible endpoints (login, signup, static resources, reset password)
                        .requestMatchers("/login", "/", "/index", "/signup", "/css/**", "/js/**", "/images/**", "/reset").permitAll()
                        // Restrict access to the admin dashboard to users with ADMIN authority
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/user/**").hasAuthority("USER")

                        .requestMatchers("/api/assets/add", "/api/assets/**").authenticated()
                        // All other requests require authentication
                        .anyRequest().authenticated()
                )

                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(
                                new AntPathRequestMatcher("/datasets/upload"),
                                new AntPathRequestMatcher("/datasets/upload-stream"),
                                new AntPathRequestMatcher("/api/assets/add"),
                                new AntPathRequestMatcher("/api/assets/update"),
                                new AntPathRequestMatcher("/api/assets/delete/**")
                        )
                )

                // Configure form-based login
                .formLogin(form -> form
                        .loginPage("/login") // Custom login page
                        .loginProcessingUrl("/process-login")
                        .usernameParameter("emailAddress") // Custom username field (email-based login)
                        .passwordParameter("password") // Custom password field
                        .successHandler(customLoginSuccessHandler)
                        .permitAll() // Allow access to the login page
                )
                // Configure logout behavior
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(customLogoutSuccessHandler)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

//    /**
//     * Defines an in-memory user details service for testing authentication.
//     * Uncomment the method to enable in-memory authentication.
//     *
//     * @return An instance of UserDetailsService with predefined users.
//     */
//        @Bean
//        @Primary  // Marks this as the preferred UserDetailsService bean
//        public UserDetailsService userDetailsService() {
//        UserDetails userDetails = User.builder()
//                .username("user")
//                .password(passwordEncoder().encode("password")) // Encode password using BCrypt
//                .roles("USER") // Assign USER role
//                .build();
//
//
//        UserDetails adminDetails = User.builder()
//                .username("mustafakamran46@hotmail.com")
//                .password(passwordEncoder().encode("password")) // Encode password
//                .authorities("ADMIN") // Assign ADMIN role
//                .build();
//
//        return new InMemoryUserDetailsManager(userDetails, adminDetails);
//        }

        /**
         * Configures the password encoder to use BCrypt hashing.
         *
         * @return A PasswordEncoder instance for securely hashing passwords.
         */
        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public WebMvcConfigurer corsConfigurer() {
            return new WebMvcConfigurer() {
                @Override
                public void addCorsMappings(CorsRegistry registry) {
                    registry.addMapping("/**")
                            .allowedOrigins("http://localhost:8080") // change as per your frontend origin
                            .allowedMethods("GET", "POST", "PUT", "DELETE")
                            .allowCredentials(true);
                }
            };
        }

}
