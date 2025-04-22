   
        
        
        
        
package com.scm.scm20.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.scm.scm20.UserServiceImpl.SecurityCustomUserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private SecurityCustomUserDetailService userDetailService;
    
    @Autowired
    private OAuthenticationSuccessHandler authenticationSuccessHandler;

    // Define the authentication provider
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    // Define password encoder bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configure security filter chain (with CSRF disabled)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .csrf().disable()  // Disable CSRF protection
            .authorizeRequests(authorize -> {
                authorize.requestMatchers("/user/**").authenticated();  // Protect /user/** endpoints
                authorize.anyRequest().permitAll();  // Allow all other requests
            })
            .formLogin(formLogin -> {
                formLogin.loginPage("/login")  // Custom login page if you have one
                         .permitAll()           // Allow anyone to access login page
                         .loginProcessingUrl("/authenticate")
                         .defaultSuccessUrl("/user/profile", true)  // Redirect after successful login
                         .failureUrl("/login?error=true")  // Optional: specify a failure page
                         .usernameParameter("email")  // Custom username parameter
                         .passwordParameter("password");  // Custom password parameter
            })
            .logout(logout -> {
                // Enable logout support, redirect to home after logout
                logout.logoutSuccessUrl("/login?logout=true").permitAll();  // Logout success URL
            })

            // OAuth2 Login configuration
            .oauth2Login(oauth2Login -> {
                oauth2Login
                    .loginPage("/login")  // Optional: Custom login page URL
                    .permitAll()  // Allow anyone to access the login page
                    .defaultSuccessUrl("/profile", true)
                    .successHandler(authenticationSuccessHandler) 
                    .failureUrl("/login?error=true");  // Redirect to failure page
            });
        httpSecurity.logout(logout -> {
            logout.logoutUrl("/logout")
                  .logoutSuccessUrl("/login?logout=true") // Redirect to login page after logout
                  .invalidateHttpSession(true) // Clear session
                  .deleteCookies("JSESSIONID"); // Remove cookies
        });


        return httpSecurity.build();
    }
}
