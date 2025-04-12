package com.scm.scm20.helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class Helper {
	public static String getEmailOfLoggedInUser(Authentication authentication) {
	    if (authentication instanceof OAuth2AuthenticationToken) {
	        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
	        OAuth2User oAuth2User = oauthToken.getPrincipal();
	        String clientId = oauthToken.getAuthorizedClientRegistrationId();

	        if ("google".equalsIgnoreCase(clientId)) {
	            System.out.println("Getting email from Google");
	            return oAuth2User.getAttribute("email");        
	        }
	        
	        
	        else if ("github".equalsIgnoreCase(clientId)) {
	            System.out.println("Getting email from GitHub");
	            String email = oAuth2User.getAttribute("email");
	            
	            if (email == null || email.isEmpty()) {
	                // Generate pseudo email from login
	                String login = oAuth2User.getAttribute("login");
	                return login != null ? login + "@example.com" : null;
	            }
	            return email;
	        }
	    }

	    System.out.println("Getting data from local database");
	    return authentication.getName();
	}


}