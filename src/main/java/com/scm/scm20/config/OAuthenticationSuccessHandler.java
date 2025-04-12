package com.scm.scm20.config;


import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.LoggerFactory;


import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.scm20.Repository.UserRepository;
import com.scm.scm20.entity.Provider;
import com.scm.scm20.entity.User;
import com.scm.scm20.helper.AppConstants;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;




import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.LoggerFactory;


import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.scm20.Repository.UserRepository;
import com.scm.scm20.entity.Provider;
import com.scm.scm20.entity.User;
import com.scm.scm20.helper.AppConstants;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@Component
public class OAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(OAuthenticationSuccessHandler.class);

    @Autowired
    public OAuthenticationSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // Convert Authentication object to OAuth2AuthenticationToken
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

        // Identify which provider the user authenticated with (Google, GitHub, LinkedIn, etc.)
        String providerId = oauthToken.getAuthorizedClientRegistrationId();
        logger.info("Provider: " + providerId);

        // Retrieve the authenticated user's details
        DefaultOAuth2User oauthUser = (DefaultOAuth2User) authentication.getPrincipal();

        // Log the user's attributes for debugging
        oauthUser.getAttributes().forEach((key, value) -> logger.info(key + ": " + value));

        // Variables to store extracted user information
        String email = null, name = null, picture = null, about = null, providerUserId = null;
        Provider provider = null;  // Provider source (e.g., GOOGLE, GITHUB, LINKEDIN)

        // Extract user details based on the provider
        if ("google".equalsIgnoreCase(providerId)) {
            providerUserId = oauthUser.getAttribute("sub");  // Google uses "sub" as unique user ID
            email = oauthUser.getAttribute("email");
            name = oauthUser.getAttribute("name");
            picture = oauthUser.getAttribute("picture");
            about = "This account has been created using Google";
            provider = Provider.GOOGLE;
        } 
        else if ("github".equalsIgnoreCase(providerId)) {
       // 	providerUserId = String.valueOf(oauthUser.getAttribute("id"));
        	Object githubId = oauthUser.getAttribute("id");
        	providerUserId = githubId != null ? String.valueOf(githubId) : null;

        	
            email = oauthUser.getAttribute("email") != null
                    ? oauthUser.getAttribute("email")
                    : oauthUser.getAttribute("login") + "@example.com";  // Fallback if email is private
            name = oauthUser.getAttribute("login");  // GitHub username
            picture = oauthUser.getAttribute("avatar_url");  // GitHub profile picture
            about = "This account has been created using GitHub";
            provider = Provider.GITHUB;
        } 
        else if ("linkedin".equalsIgnoreCase(providerId)) {
            providerUserId = String.valueOf(oauthUser.getAttribute("id"));  // Convert to String
            email = oauthUser.getAttribute("email");  // Ensure you have email permission in LinkedIn OAuth
            name = oauthUser.getAttribute("localizedFirstName") + " " + oauthUser.getAttribute("localizedLastName");
            picture = oauthUser.getAttribute("profilePicture");
            about = "This account has been created using LinkedIn";
            provider = Provider.LINKEDIN;
        }

        // Check if essential user data is missing
        if (email == null || name == null) {
            logger.error("Email or Name is missing. Authentication failed.");
            response.sendRedirect("/login?error=missing_details");
            return;  // Stop further execution
        }

        // Check if user already exists in the database
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isEmpty()) {  // If user does not exist, create a new one
            User user = new User();
            user.setUserId(UUID.randomUUID().toString());  // Generate unique ID
            user.setEmail(email);
            user.setName(name);
            user.setProfilePic(picture);
            user.setAbout(about);
            user.setProvider(provider);
            user.setProviderUserId(providerUserId);  // Save provider user ID correctly
            user.setRoleList(List.of(AppConstants.ROLE_USER)); // Assign default role
            user.setEmailVerified(true);
            user.setEnabled(true);

            // Generate a random password to satisfy constraints, as OAuth does not provide one
            user.setPassword(UUID.randomUUID().toString());

            // Save new user to the database
            userRepository.save(user);
        }
        response.sendRedirect("/user/dashboard");
    }
}

 

//    
           
    	
//        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
//        Map<String, Object> attributes = user.getAttributes();
//
//        String email = (String) attributes.get("email");
//        String name = (String) attributes.get("name");
//        String picture = (String) attributes.get("picture");
//
//        System.out.println("User logged in: " + email);
//        System.out.println("Name: " + name);
//        System.out.println("Picture: " + picture);
//
//        // Check if user already exists
//        Optional<User> existingUserOptional = userRepository.findByEmail(email);
//        if (existingUserOptional.isEmpty()) {
//            // Create new user
//            User newUser = new User();
//            newUser.setUserId(UUID.randomUUID().toString());
//            newUser.setEmail(email);
//            newUser.setName(name);
//            newUser.setProfilePic(picture);
//            newUser.setPassword(UUID.randomUUID().toString());  // Generate random password
//            newUser.setProvider(Provider.GOOGLE);
//            newUser.setEnabled(true);
//            newUser.setEmailVerified(true);
//            newUser.setRoleList(List.of(AppConstants.ROLE_USER));
//            newUser.setAbout("This account is created using Google OAuth");
//
//            System.out.println("Saving new user: " + newUser);
//            userRepository.save(newUser);
//        } else {
//            System.out.println("User already exists in database.");
//        }

//        response.sendRedirect("/user/dashboard");
//    }
//}
