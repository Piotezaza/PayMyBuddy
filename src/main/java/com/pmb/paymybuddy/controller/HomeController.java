package com.pmb.paymybuddy.controller;

import com.pmb.paymybuddy.model.Contact;
import com.pmb.paymybuddy.model.Transaction;
import com.pmb.paymybuddy.model.User;
import com.pmb.paymybuddy.service.ContactService;
import com.pmb.paymybuddy.service.TransactionService;
import com.pmb.paymybuddy.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Controller
public class HomeController {

    @Autowired
    UserService userService;

    @Autowired
    ContactService contactService;

    @Autowired
    TransactionService transactionService;

    @GetMapping("/home")
    public ModelAndView homeView(Model model, HttpServletRequest request, @RequestParam(defaultValue = "0") int page) {
        log.trace("GET request on /home");

        ModelAndView mv = new ModelAndView();

        Principal principal = request.getUserPrincipal();
        User userConnected = userService.getUserByEmail(principal.getName());
        List<Contact> contacts = contactService.getContactsByUser(userConnected);

        int size = 5; // Nombre de transactions par page
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Page<Transaction> transactionsPage = transactionService.getAllTransactionsForAccountPageable(userConnected.getComptePMB(), pageable);

        model.addAttribute("user", userConnected);
        model.addAttribute("contacts", contacts);
        model.addAttribute("transactions", transactionsPage);
        model.addAttribute("totalPages", transactionsPage.getTotalPages());
        model.addAttribute("balance", userService.getBalance(userConnected));

        mv.setViewName("home");
        return mv;
    }

    @PostMapping("/addConnection")
    public ModelAndView addUserConnection(@ModelAttribute("contact") @Valid Contact contact, HttpServletRequest request, Errors errors, Model model) {
        try {
            Principal principal = request.getUserPrincipal();
            User userConnected = userService.getUserByEmail(principal.getName());

            String email = request.getParameter("connectionInput");

            User userToAdd = userService.getUserByEmail(email);

            if (userToAdd == null) {
                return new ModelAndView("redirect:/home?error");
            } else {
                if (contactService.findByUserAndContact(userConnected, userToAdd) != null) {
                    return new ModelAndView("redirect:/home?connectionAlreadyExist");
                } else {
                    Contact connection = new Contact();
                    connection.setUser(userConnected);
                    connection.setContact(userToAdd);
                    contactService.addContact(connection);

                    return new ModelAndView("redirect:/home?successConnection");
                }
            }
        } catch (Exception exception) {
            return new ModelAndView("redirect:/home?error");
        }
    }

    @PostMapping("/pay")
    public ModelAndView payAContact(@ModelAttribute("transaction") @Valid Transaction transaction, HttpServletRequest request, Errors errors, Model model) {
        try {
            Principal principal = request.getUserPrincipal();
            User userIssuer = userService.getUserByEmail(principal.getName());
            User userRecipient = userService.getUserById(Integer.valueOf(request.getParameter("contact")));
            BigDecimal montant = new BigDecimal(request.getParameter("amount"));

            if (!isTransactionPossible(userIssuer, montant)) {
                return new ModelAndView("redirect:/home?error");
            }

            LocalDateTime now = LocalDateTime.now();
            Transaction newTransaction = new Transaction();
            newTransaction.setMotif(request.getParameter("motif"));
            newTransaction.setMontant(new BigDecimal(request.getParameter("amount")));
            newTransaction.setDate(now);
            newTransaction.setIssuer(userIssuer.getComptePMB());
            newTransaction.setRecipient(userRecipient.getComptePMB());

            transactionService.addTransaction(newTransaction);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return new ModelAndView("redirect:/home?error");
        }
        return new ModelAndView("redirect:/home?successPayment");
    }

    // Les méthodes en dessous pourraient aller dans une classe TransactionBusiness du package "business"
    public boolean isTransactionPossible(User userIssuer, BigDecimal montant) {
        if (!isSoldeSuffisant(userIssuer, montant)) {
            return false;
        }

        if (!isMontantValide(montant)) {
            return false;
        }

        return true;
    }

    public boolean isSoldeSuffisant(User userIssuer, BigDecimal montant) {
        // retourne -1 si le montant est supérieur au solde | 0 si égal | 1 si montant est inférieur au solde
        return montant.compareTo(userService.getBalance(userIssuer)) <= 0;
    }

    public boolean isMontantValide(BigDecimal montant) {
        return montant.compareTo(BigDecimal.ZERO) > 0;
    }
}