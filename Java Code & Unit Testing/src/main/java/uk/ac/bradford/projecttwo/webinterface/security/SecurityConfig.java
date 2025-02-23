package uk.ac.bradford.projecttwo.webinterface.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final Encryptor encryptor;

    public SecurityConfig(Encryptor encryptor) {
        this.encryptor = encryptor;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/index", "/signup", "/css/**", "/js/**", "/images/**").permitAll() // Allow access to index and static resources
                .requestMatchers("/admin/dashboard").hasAuthority("ADMIN") // Restrict /admin/dashboard to ADMIN
                .anyRequest().authenticated() // Require authentication for all other endpoints
            )
            .httpBasic(Customizer.withDefaults()) // Enable HTTP Basic authentication
            // .formLogin(form -> form
            //     .loginPage("/login") // Custom login page
            //     .permitAll() // Allow access to the login page
            // )
            .logout(logout -> logout
                .permitAll() // Allow access to the logout endpoint
            );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails userDetails = User.builder()
            .username("user")
            .password(passwordEncoder().encode("password")) // Use PasswordEncoder to hash the password
            .roles("USER")
            .build();

        UserDetails adminDetails = User.builder()
            .username("mustafakamran46@hotmail.com")
            .password(passwordEncoder().encode("password")) // Use PasswordEncoder to hash the password
            .authorities("ADMIN")
            .build();

        return new InMemoryUserDetailsManager(userDetails, adminDetails);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Use BCrypt for password encoding
    }
}