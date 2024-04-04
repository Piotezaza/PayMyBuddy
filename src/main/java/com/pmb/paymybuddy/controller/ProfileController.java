package com.pmb.paymybuddy.controller;

import com.pmb.paymybuddy.model.User;
import com.pmb.paymybuddy.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class ProfileController {

    @Autowired
    UserService userService;

    @GetMapping("/profile")
    public ModelAndView getTestData(Model model, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        Principal principal = request.getUserPrincipal();

        User user = userService.getUserByEmail(principal.getName());

        model.addAttribute("user", user);

        mv.setViewName("profile");

        return mv;
    }

    @PostMapping("/profile")
    public ModelAndView saveUser(@ModelAttribute("user") @Valid User user, HttpServletRequest request, Errors errors, Model model) {
        try {
            System.out.println(user);
            userService.saveUser(user);
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return new ModelAndView().addObject("message", exception.getMessage());
        }

        return new ModelAndView("redirect:/profile?success", "user", user);
    }
}
