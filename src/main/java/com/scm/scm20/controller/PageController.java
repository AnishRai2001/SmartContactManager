package com.scm.scm20.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.scm.scm20.entity.User;
import com.scm.scm20.helper.Message;
import com.scm.scm20.helper.MessageType;
import jakarta.validation.Valid;
import com.scm.scm20.UserService.UserService;

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
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/do-register")
    public String processRegister(@Valid @ModelAttribute("user") User user, 
                                 BindingResult bindingResult, 
                                 RedirectAttributes redirectAttributes, 
                                 Model model) {
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }

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

            // Save user (without password encoding)
            userService.saveUser(user);

            redirectAttributes.addFlashAttribute("message", 
                new Message("Registration Successful!", MessageType.SUCCESS));
            return "redirect:/register";

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", 
                new Message("Something went wrong! Please try again.", MessageType.ERROR));
            return "redirect:/register";
        }
    }
}
