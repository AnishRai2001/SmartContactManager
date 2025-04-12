package com.scm.scm20.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.scm20.Exception.ResourceNotFoundException;
import com.scm.scm20.UserService.UserService;
import com.scm.scm20.entity.User;
import com.scm.scm20.helper.Helper;

@ControllerAdvice
public class Rootcontroller {
	
	@Autowired
	private UserService userService;
	
	private Logger logger=LoggerFactory.getLogger(this.getClass());
	 @ModelAttribute
	    public void addLoggedinUserInformation(Model model,Authentication authentication) {
	    	System.out.println("adding logged in user");
	    	if (authentication == null || !authentication.isAuthenticated()) {
	            model.addAttribute("loggedinUser", null);
	            return;
	        }
	      String userName = Helper.getEmailOfLoggedInUser(authentication);
	      logger.info("User logged in: {}", userName);

	      if (userName == null || userName.isEmpty()) {
	          model.addAttribute("errorMessage", "No email found for logged-in user.");
	         
	      }

	      try {
	          User user = userService.getUserByEmail(userName);
	          if (user == null) {
	              throw new ResourceNotFoundException("User not found with email: " + userName);
	          }

	          model.addAttribute("loggedinUser", user);
	        
	      } catch (ResourceNotFoundException e) {
	          logger.error("User not found: {}", userName);
	          model.addAttribute("errorMessage", "User not found in the system.");
	       
	      }
	  }
}
