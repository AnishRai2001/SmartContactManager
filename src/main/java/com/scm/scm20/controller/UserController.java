package com.scm.scm20.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.scm20.Exception.ResourceNotFoundException;
import com.scm.scm20.UserService.UserService;
import com.scm.scm20.entity.User;
import com.scm.scm20.helper.Helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/dashboard")
    public String userDashboard() {
        return "user/dashboard"; // Thymeleaf template for dashboard
    }
    
    @GetMapping("profile")
       public String userProfile(Model model, Authentication authentication) {
        return  "user/profile";
    }
    
    
//    @ModelAttribute
//    public void addLoggedinUserInformation(Model model,Authentication authentication) {
//    	System.out.println("adding logged in user");
//      String userName = Helper.getEmailOfLoggedInUser(authentication);
//      logger.info("User logged in: {}", userName);
//
//      if (userName == null || userName.isEmpty()) {
//          model.addAttribute("errorMessage", "No email found for logged-in user.");
//         
//      }
//
//      try {
//          User user = userService.getUserByEmail(userName);
//          if (user == null) {
//              throw new ResourceNotFoundException("User not found with email: " + userName);
//          }
//
//          model.addAttribute("loggedinUser", user);
//        
//      } catch (ResourceNotFoundException e) {
//          logger.error("User not found: {}", userName);
//          model.addAttribute("errorMessage", "User not found in the system.");
//       
//      }
//  }
    }
   

    
    
//    @GetMapping("/profile")
//    public String userProfile(Model model, Authentication authentication) {
//        String userName = Helper.getEmailOfLoggedInUser(authentication);
//        logger.info("User logged in: {}", userName);
//
//        if (userName == null || userName.isEmpty()) {
//            model.addAttribute("errorMessage", "No email found for logged-in user.");
//            return "error"; // Ensure "error.html" exists in templates
//        }
//
//        try {
//            User user = userService.getUserByEmail(userName);
//            if (user == null) {
//                throw new ResourceNotFoundException("User not found with email: " + userName);
//            }
//
//            model.addAttribute("loggedinUser", user);
//            return "user/profile"; // Ensure "user/profile.html" exists in templates
//        } catch (ResourceNotFoundException e) {
//            logger.error("User not found: {}", userName);
//            model.addAttribute("errorMessage", "User not found in the system.");
//            return "error";
//        }
//    }

