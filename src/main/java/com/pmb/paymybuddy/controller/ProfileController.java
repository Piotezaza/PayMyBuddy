package com.pmb.paymybuddy.controller;

import com.pmb.paymybuddy.model.CompteBancaire;
import com.pmb.paymybuddy.model.User;
import com.pmb.paymybuddy.service.CompteBancaireService;
import com.pmb.paymybuddy.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;

@Slf4j
@Controller
public class ProfileController {

    @Autowired
    UserService userService;

    @Autowired
    CompteBancaireService compteBancaireService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/profile")
    public ModelAndView getTestData(Model model, HttpServletRequest request) {
        log.trace("GET request on /profile");
        ModelAndView mv = new ModelAndView();
        Principal principal = request.getUserPrincipal();
        User user = userService.getUserByEmail(principal.getName());

        model.addAttribute("user", user);

        mv.setViewName("profile");

        return mv;
    }

    @PostMapping("/profile/user")
    public ModelAndView updateUser(@ModelAttribute("user") @Valid User user, HttpServletRequest request, Errors errors, Model model) {
        try {
            Principal principal = request.getUserPrincipal();
            User userConnected = userService.getUserByEmail(principal.getName());
            userConnected.setEmail(user.getEmail());
            userConnected.setPrenom(user.getPrenom());
            userConnected.setNom(user.getNom());

            userService.saveUser(userConnected);
            log.info("User updated: " + userConnected.toString());
        } catch (Exception exception) {
            return new ModelAndView("redirect:/profile?error");
        }

        return new ModelAndView("redirect:/profile?successProfile", "user", user);
    }

    @PostMapping("/profile/bankAccount")
    public ModelAndView updateUserBank(@ModelAttribute("user") @Valid User user, HttpServletRequest request, Errors errors, Model model) {
        try {
            Principal principal = request.getUserPrincipal();
            User userConnected = userService.getUserByEmail(principal.getName());

            CompteBancaire newCB = user.getCompteBancaire();
            String newIban = newCB.getIban();
            CompteBancaire oldCB = userConnected.getCompteBancaire();
            oldCB.setIban(newIban);
            userConnected.setCompteBancaire(oldCB);

            userService.saveUser(userConnected);
            log.info("User updated: " + userConnected.toString());
        } catch (Exception exception) {
            return new ModelAndView("redirect:/profile?error");
        }

        return new ModelAndView("redirect:/profile?successIban", "user", user);
    }

    @PostMapping("/profile/passwordEdition")
    public ModelAndView updateUserPassword(@ModelAttribute("user") @Valid User user, HttpServletRequest request, Errors errors, Model model) {
        try {
            Principal principal = request.getUserPrincipal();
            User userConnected = userService.getUserByEmail(principal.getName());

            userConnected.setPassword(passwordEncoder.encode(user.getPassword()));

            userService.saveUser(userConnected);
            log.info("User updated: " + userConnected.toString());
        } catch (Exception exception) {
            return new ModelAndView("redirect:/profile?error");
        }

        return new ModelAndView("redirect:/profile?successPassword", "user", user);
    }
}
