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
                        // Uncomment the line below if you wish to permit access to static resources and other endpoints
                        .requestMatchers("/login" ,"/", "/index", "/signup", "/css/**", "/js/**", "/images/**","/reset").permitAll()
                        .requestMatchers("/admin/dashboard").hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/index",true)
                        .usernameParameter("emailAddress")
                        .passwordParameter("password")
                        .permitAll()
                )
                .logout(logout -> logout
                        .permitAll()
                );

        return http.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails userDetails = User.builder()
//                .username("user")
//                .password(passwordEncoder().encode("password"))
//                .roles("USER")
//                .build();

//        UserDetails adminDetails = User.builder()
//                .username("mustafakamran46@hotmail.com")
//                .password(passwordEncoder().encode("password"))
//                .authorities("ADMIN")
//                .build();

//        return new InMemoryUserDetailsManager(userDetails, adminDetails);
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
