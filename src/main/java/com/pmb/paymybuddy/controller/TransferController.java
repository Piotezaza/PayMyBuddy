package com.pmb.paymybuddy.controller;

import com.pmb.paymybuddy.model.User;
import com.pmb.paymybuddy.model.Virement;
import com.pmb.paymybuddy.service.ComptePMBService;
import com.pmb.paymybuddy.service.UserService;
import com.pmb.paymybuddy.service.VirementService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;

@Slf4j
@Controller
public class TransferController {

    @Autowired
    UserService userService;

    @Autowired
    VirementService virementService;

    @Autowired
    ComptePMBService comptePMBService;

    @GetMapping("/transfer")
    public ModelAndView transferView(Model model, HttpServletRequest request,
                                     @RequestParam(defaultValue = "0") int page) {
        log.trace("GET request on /transfer");
        ModelAndView mv = new ModelAndView();

        Principal principal = request.getUserPrincipal();
        User userConnected = userService.getUserByEmail(principal.getName());

        int size = 5; // Nombre de virements par page
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Page<Virement> virementsPage = virementService.getAllVirementsForAccountPageable(userConnected.getCompteBancaire(), pageable);

        model.addAttribute("user", userConnected);
        model.addAttribute("virements", virementsPage);
        model.addAttribute("totalPages", virementsPage.getTotalPages());
        model.addAttribute("balance", userService.getBalance(userConnected));

        mv.setViewName("transfer");
        return mv;
    }

    @PostMapping("/transfer")
    public ModelAndView transfer(Model model, HttpServletRequest request) {
        log.trace("POST request on /transfer");
        Principal principal = request.getUserPrincipal();
        User user = userService.getUserByEmail(principal.getName());
        BigDecimal montant = new BigDecimal(request.getParameter("amount"));
        String type = request.getParameter("type");

        try {
            if (!isTransactionPossible(user, montant, type)) {
                return new ModelAndView("redirect:/transfer?error");
            }
            System.out.println("Request: " + request);
            Virement virement = new Virement();
            virement.setType(request.getParameter("type"));
            virement.setMontant(new BigDecimal(request.getParameter("amount")));
            virement.setDate(LocalDateTime.now());
            virement.setComptePMB(user.getComptePMB());
            virement.setCompteBancaire(user.getCompteBancaire());

            virementService.saveVirement(virement);

            log.info("User " + user.getEmail() + " has made a transfer of " + montant + " " + type + " on " + LocalDateTime.now());
            return new ModelAndView("redirect:/transfer?successTransfer");
        } catch (NumberFormatException e) {
            return new ModelAndView("redirect:/transfer?error");
        }
    }

    // Les méthodes en dessous pourraient aller dans une classe TransactionBusiness du package "business"
    public boolean isTransactionPossible(User userIssuer, BigDecimal montant, String type) {
        if (!isMontantValid(montant)) {
            return false;
        }

        if (!isIBANCompleted(userIssuer)) {
            return false;
        }

        return true;
    }

    public boolean isMontantValid(BigDecimal montant) {
        return montant.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isIBANCompleted(User userIssuer) {
        return !userIssuer.getCompteBancaire().getIban().isEmpty();
    }
}
