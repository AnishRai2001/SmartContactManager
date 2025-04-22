package com.scm.scm20.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.scm.scm20.entity.User;

import jakarta.validation.Valid;

import com.scm.scm20.UserService.UserService;
import com.scm.scm20.UserForm.UserForm;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("name", "Anish");
        model.addAttribute("github", "githubAnish");
        return "home";
    }

    @GetMapping("/service")
    public String servicePage() {
        return "service";
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }

    @GetMapping("/contact")
    public String contactPage() {
        return "contact";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/check-email")
    public Boolean checkEmail(@RequestParam String email) {
        return userService.isEmailTaken(email);
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "register";
    }

    @PostMapping("/do-register")
    public String processRegister(@Valid @ModelAttribute("userForm") UserForm userForm,BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userForm", userForm); // Pass user back to the model for form errors
            return "register"; // Return to the registration page
        }
        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());

        try {
        	 // Check if email is already registered
            if (userService.isEmailTaken(user.getEmail())) {
                bindingResult.rejectValue("email", "error.user", "Email is already registered!");
                return "register";
            }

            // Check if phone number is already registered
            if (userService.isPhoneNumberTaken(user.getPhoneNumber())) {
                bindingResult.rejectValue("phoneNumber", "error.user", "Phone number is already registered!");
                return "register";
            }
         // Check if name is already registered
            if (userService.isNameTaken(user.getName())) {
                bindingResult.rejectValue("name", "error.user", "Name is already present!");
                return "register";
            }

        	
        	
            userService.saveUser(user);
            redirectAttributes.addFlashAttribute("message", "Registration Successful!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Something went wrong! Please try again.");
            redirectAttributes.addFlashAttribute("messageType", "error");
        }

        return "redirect:/register";
    }
}

    
   
//    @PostMapping("/do-register")
//    public String processRegister(@ModelAttribute("userForm") UserForm userForm) {
//        // Create a new User object from the UserForm
//        User user = new User();
//        user.setName(userForm.getName());
//        user.setEmail(userForm.getEmail());
//        user.setPassword(userForm.getPassword());
//        user.setAbout(userForm.getAbout());
//        user.setPhoneNumber(userForm.getPhoneNumber());
//
//        // Save the user
//        userService.saveUser(user);
//
//        return "redirect:/login"; // Redirect to the login page after successful registration
//    }
//}

    
    
    
    
    

    
////
//    @PostMapping("/do-register")
//    public String processRegister(@Valid @ModelAttribute("user") User user, 
//                                 BindingResult bindingResult, 
//                                 RedirectAttributes redirectAttributes, 
//                                 Model model) {
//    	System.out.println("working");
//        
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("user", user); // Pass user back to the model for form errors
//            return "register"; // Return to the regisFtration page
//        }
//
//        // Check if email is already registered
//        if (userService.isEmailTaken(user.getEmail())) {
//            bindingResult.rejectValue("email", "error.user", "Email is already registered!");
//            return "register";
//        }
//
//        // Check if phone number is already registered
//        if (userService.isPhoneNumberTaken(user.getPhoneNumber())) {
//            bindingResult.rejectValue("phoneNumber", "error.user", "Phone number is already registered!");
//            return "register";
//        }
//
//        try {
//            // Save user (ensure password encoding if needed)
//            userService.saveUser(user);
//
//            redirectAttributes.addFlashAttribute("message", 
//                new Message("Registration Successful!", MessageType.SUCCESS));
//            return "redirect:/register"; // Redirect to the registration page to show success
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            redirectAttributes.addFlashAttribute("message", 
//                new Message("Something went wrong! Please try again.", MessageType.ERROR));
//            return "redirect:/register"; // Return to the registration page with error message
//        }
//    }
//}
