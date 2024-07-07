package com.pmb.paymybuddy.controller;

import com.pmb.paymybuddy.model.User;
import com.pmb.paymybuddy.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Slf4j
@Controller
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public ModelAndView login() {
        log.info("User is on the login page.");
        return new ModelAndView("login");
    }

    @GetMapping("/registration")
    public String showRegistrationForm(WebRequest request, Model model) {
        log.info("User is on the registration page.");
        User user = new User();
        model.addAttribute("user", user);
        return "registration";
    }

    @PostMapping("/registration")
    public ModelAndView saveUser(@ModelAttribute("user") @Valid User user, HttpServletRequest request, Errors errors, Model model) {
        try {
            log.info("User {} has created an account.", user.getEmail());
            userService.addUser(user);
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return new ModelAndView().addObject("message", exception.getMessage());
        }

        return new ModelAndView("redirect:/login?success", "user", user);
    }
}