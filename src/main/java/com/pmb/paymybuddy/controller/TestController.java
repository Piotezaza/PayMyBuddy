package com.pmb.paymybuddy.controller;

import com.pmb.paymybuddy.model.User;
import com.pmb.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestController {

    @Autowired
    UserService userService;

    @GetMapping("/home")
    public ModelAndView getTestData(Model model) {
        ModelAndView mv = new ModelAndView();

        // TODO : adapter avec l'utilisateur connecté
        User user = userService.getUserById(0);

        model.addAttribute("user", user);

        mv.setViewName("home");

        return mv;
    }
}