package com.pmb.paymybuddy.unit.controller;

import com.pmb.paymybuddy.controller.HomeController;
import com.pmb.paymybuddy.controller.TransferController;
import com.pmb.paymybuddy.model.*;
import com.pmb.paymybuddy.service.ContactService;
import com.pmb.paymybuddy.service.TransactionService;
import com.pmb.paymybuddy.service.UserService;
import com.pmb.paymybuddy.service.VirementService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionTest {

    @InjectMocks
    HomeController homeController;

    @Mock
    HomeController homeC;

    @InjectMocks
    TransferController transferController;

    @Mock
    TransferController transferC;

    @Mock
    UserService userService;

    @Mock
    ContactService contactService;
    @Mock
    VirementService virementService;

    @Mock
    TransactionService transactionService;

    @Mock
    HttpServletRequest request;

    @Mock
    Model model;

    @Mock
    Principal principal;

    @Mock
    BindingResult bindingResult;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        request = mock(HttpServletRequest.class);
    }

    @Test
    @DisplayName("Should return home view with correct model attributes and paginated transactions")
    public void homeViewWithPaginatedTransactionsTest() {
        when(request.getUserPrincipal()).thenReturn(principal);
        when(principal.getName()).thenReturn("test@test.com");

        User user = new User();
        when(userService.getUserByEmail("test@test.com")).thenReturn(user);

        List<Contact> contacts = new ArrayList<>();
        when(contactService.getContactsByUser(user)).thenReturn(contacts);

        int page = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Page<Transaction> transactionsPage = new PageImpl<>(new ArrayList<>(), pageable, 10);
        when(transactionService.getAllTransactionsForAccountPageable(user.getComptePMB(), pageable)).thenReturn(transactionsPage);

        ModelAndView modelAndView = homeController.homeView(model, request, page);
        assertEquals("home", modelAndView.getViewName());
        verify(model, times(1)).addAttribute("user", user);
        verify(model, times(1)).addAttribute("contacts", contacts);
        verify(model, times(1)).addAttribute("transactions", transactionsPage);
        verify(model, times(1)).addAttribute("totalPages", transactionsPage.getTotalPages());
    }

    @Test
    @DisplayName("Should return home view with correct model attributes and no transactions")
    public void homeViewWithNoTransactionsTest() {
        when(request.getUserPrincipal()).thenReturn(principal);
        when(principal.getName()).thenReturn("test@test.com");

        User user = new User();
        when(userService.getUserByEmail("test@test.com")).thenReturn(user);

        List<Contact> contacts = new ArrayList<>();
        when(contactService.getContactsByUser(user)).thenReturn(contacts);

        int page = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Page<Transaction> transactionsPage = Page.empty(pageable);
        when(transactionService.getAllTransactionsForAccountPageable(user.getComptePMB(), pageable)).thenReturn(transactionsPage);

        ModelAndView modelAndView = homeController.homeView(model, request, page);
        assertEquals("home", modelAndView.getViewName());
        verify(model, times(1)).addAttribute("user", user);
        verify(model, times(1)).addAttribute("contacts", contacts);
        verify(model, times(1)).addAttribute("transactions", transactionsPage);
        verify(model, times(1)).addAttribute("totalPages", transactionsPage.getTotalPages());
    }

    @Test
    @DisplayName("Should redirect to home view with error when user to add does not exist")
    public void addUserConnectionUserDoesNotExist() {
        when(request.getUserPrincipal()).thenReturn(principal);
        when(principal.getName()).thenReturn("test@test.com");

        User user = new User();
        user.setEmail("test@test.com");
        when(userService.getUserByEmail("test@test.com")).thenReturn(user);

        when(userService.getUserByEmail("nonexistent@test.com")).thenReturn(null);

        Contact contact = new Contact();
        when(request.getParameter("connectionInput")).thenReturn("nonexistent@test.com");
        ModelAndView modelAndView = homeController.addUserConnection(contact, request, bindingResult, model);
        assertEquals("redirect:/home?error", modelAndView.getViewName());
    }

    @Test
    @DisplayName("Should redirect to home view with successConnection when user to add exists and is not already a contact")
    public void addUserConnectionUserExistsAndIsNotAContact() {
        when(request.getUserPrincipal()).thenReturn(principal);
        when(principal.getName()).thenReturn("test@test.com");

        User user = new User();
        user.setEmail("test@test.com");
        when(userService.getUserByEmail("test@test.com")).thenReturn(user);

        User userToAdd = new User();
        userToAdd.setEmail("newcontact@test.com");
        when(userService.getUserByEmail("newcontact@test.com")).thenReturn(userToAdd);

        when(contactService.findByUserAndContact(user, userToAdd)).thenReturn(null);

        Contact contact = new Contact();
        when(request.getParameter("connectionInput")).thenReturn("newcontact@test.com");
        ModelAndView modelAndView = homeController.addUserConnection(contact, request, bindingResult, model);
        assertEquals("redirect:/home?successConnection", modelAndView.getViewName());
    }

    @Test
    @DisplayName("Should redirect to home view with connectionAlreadyExist when user to add is already a contact")
    public void addUserConnectionUserAlreadyAContact() {
        when(request.getUserPrincipal()).thenReturn(principal);
        when(principal.getName()).thenReturn("test@test.com");

        User user = new User();
        user.setEmail("test@test.com");
        when(userService.getUserByEmail("test@test.com")).thenReturn(user);

        User userToAdd = new User();
        userToAdd.setEmail("existingcontact@test.com");
        when(userService.getUserByEmail("existingcontact@test.com")).thenReturn(userToAdd);

        Contact existingContact = new Contact();
        existingContact.setUser(user);
        existingContact.setContact(userToAdd);
        when(contactService.findByUserAndContact(user, userToAdd)).thenReturn(existingContact);

        Contact contact = new Contact();
        when(request.getParameter("connectionInput")).thenReturn("existingcontact@test.com");
        ModelAndView modelAndView = homeController.addUserConnection(contact, request, bindingResult, model);
        assertEquals("redirect:/home?connectionAlreadyExist", modelAndView.getViewName());
    }

    @Test
    @DisplayName("Should redirect to home view with successPayment when transaction is possible")
    public void payAContactTransactionPossible() throws Exception {
        when(request.getUserPrincipal()).thenReturn(principal);
        when(principal.getName()).thenReturn("test@test.com");

        User userIssuer = new User();
        userIssuer.setEmail("test@test.com");
        when(userService.getUserByEmail("test@test.com")).thenReturn(userIssuer);

        User userRecipient = new User();
        userRecipient.setEmail("recipient@test.com");
        when(userService.getUserById(anyInt())).thenReturn(userRecipient);

        when(request.getParameter("amount")).thenReturn("100");
        when(request.getParameter("contact")).thenReturn("1");
        when(request.getParameter("motif")).thenReturn("test");

        when(userService.getBalance(userIssuer)).thenReturn(new BigDecimal("1000")); // or any other BigDecimal value
        when(homeC.isSoldeSuffisant(userIssuer, new BigDecimal("1000"))).thenReturn(true);
        when(homeC.isTransactionPossible(userIssuer, new BigDecimal("10"))).thenReturn(true);

        ModelAndView modelAndView = homeController.payAContact(new Transaction(), request, bindingResult, model);
        assertEquals("redirect:/home?successPayment", modelAndView.getViewName());
    }


    @Test
    @DisplayName("Should redirect to home view with error when exception is thrown")
    public void payAContactExceptionThrown() throws Exception {
        when(request.getUserPrincipal()).thenReturn(principal);
        when(principal.getName()).thenReturn("test@test.com");

        User userIssuer = new User();
        userIssuer.setEmail("test@test.com");
        when(userService.getUserByEmail("test@test.com")).thenReturn(userIssuer);

        when(request.getParameter("amount")).thenReturn("100");
        when(request.getParameter("contact")).thenReturn("1");
        when(request.getParameter("motif")).thenReturn("test");

        when(userService.getUserById(anyInt())).thenThrow(new RuntimeException());

        ModelAndView modelAndView = homeController.payAContact(new Transaction(), request, bindingResult, model);
        assertEquals("redirect:/home?error", modelAndView.getViewName());
    }


    @Test
    @DisplayName("Should redirect to transfer view with successTransfer when transaction is possible")
    public void transferTransactionPossible() {
        when(request.getUserPrincipal()).thenReturn(principal);
        when(principal.getName()).thenReturn("test@test.com");

        User user = new User();
        CompteBancaire compteBancaire = new CompteBancaire();
        compteBancaire.setIban("FR123456789");
        user.setCompteBancaire(compteBancaire);
        user.setEmail("test@test.com");
        when(userService.getUserByEmail("test@test.com")).thenReturn(user);

        when(request.getParameter("amount")).thenReturn("100");
        when(request.getParameter("type")).thenReturn("OUT");

        when(virementService.saveVirement(any())).thenReturn(new Virement());

        when(transferC.isTransactionPossible(user, new BigDecimal("100"), "OUT")).thenReturn(true);

        ModelAndView modelAndView = transferController.transfer(null, request);
        assertEquals("redirect:/transfer?successTransfer", modelAndView.getViewName());
    }
}
