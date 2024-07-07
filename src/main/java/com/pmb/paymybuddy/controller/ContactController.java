package com.pmb.paymybuddy.controller;

import com.pmb.paymybuddy.model.User;
import com.pmb.paymybuddy.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Slf4j
@Controller
public class ContactController {

    @Autowired
    UserService userService;
    @GetMapping("/contact")
    public ModelAndView mainContactView(Model model, HttpServletRequest request) {
        log.info("User is on the contact page.");

        ModelAndView mv = new ModelAndView();
        Principal principal = request.getUserPrincipal();
        User user = userService.getUserByEmail(principal.getName());

        model.addAttribute("user", user);

        mv.setViewName("contact");

        return mv;
    }

   @PostMapping("/contact/delete/{id}")
    public ModelAndView deleteContact(@PathVariable("id") int id, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        User user = userService.getUserByEmail(principal.getName());
        User contact = userService.getUserById(id);
        user.getContacts().remove(contact);
        userService.saveUser(user);
        log.info("User {} has deleted contact {}.", user.getEmail(), contact.getEmail());
        return new ModelAndView("redirect:/contact?successDelete");
    }
}
