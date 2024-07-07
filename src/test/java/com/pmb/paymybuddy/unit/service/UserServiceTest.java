package com.pmb.paymybuddy.unit.service;

import com.pmb.paymybuddy.model.*;
import com.pmb.paymybuddy.repository.UserRepository;
import com.pmb.paymybuddy.service.CompteBancaireService;
import com.pmb.paymybuddy.service.ComptePMBService;
import com.pmb.paymybuddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    ComptePMBService comptePMBService;

    @Mock
    CompteBancaireService compteBancaireService;

    @Mock
    PasswordEncoder passwordEncoder;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Should return user by id")
    public void getUserById() {
        User user = new User();
        user.setId(1);
        user.setEmail("test@test.com");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1);

        assertEquals(user, result);
    }

    @Test
    @DisplayName("Should throw exception when user not found by id")
    public void getUserByIdNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserById(1));
    }

    @Test
    @DisplayName("Should return user by email")
    public void getUserByEmail() {
        User user = new User();
        user.setEmail("test@test.com");
        when(userRepository.findByEmail("test@test.com")).thenReturn(user);

        User result = userService.getUserByEmail("test@test.com");

        assertEquals(user, result);
    }

    @Test
    @DisplayName("Should add user successfully")
    public void addUser() throws Exception {
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("password");

        ComptePMB comptePMB = new ComptePMB();
        when(comptePMBService.saveComptePMB(any(ComptePMB.class))).thenReturn(comptePMB);

        CompteBancaire compteBancaire = new CompteBancaire();
        when(compteBancaireService.saveCompteBancaire(any(CompteBancaire.class))).thenReturn(compteBancaire);

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.addUser(user);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when adding user with existing email")
    public void addUserEmailExists() {
        User user = new User();
        user.setEmail("test@test.com");

        when(userRepository.findByEmail("test@test.com")).thenReturn(user);

        assertThrows(Exception.class, () -> userService.addUser(user));
    }

    @Test
    @DisplayName("Should return balance correctly")
    public void getBalance() {
        User user = new User();
        ComptePMB comptePMB = new ComptePMB();
        CompteBancaire compteBancaire = new CompteBancaire();
        Transaction transaction = new Transaction();
        Virement virement = new Virement();

        transaction.setDate(LocalDateTime.now());
        transaction.setMontant(new BigDecimal("100"));
        transaction.setFrais(new BigDecimal("10"));
        List<Transaction> debits = List.of(transaction);
        List<Transaction> credits = List.of(transaction, transaction);

        virement.setMontant(new BigDecimal("1000"));
        virement.setDate(LocalDateTime.now());
        virement.setType("IN");
        virement.setComptePMB(comptePMB);
        List<Virement> virements = List.of(virement);

        comptePMB.setDebits(debits);
        comptePMB.setCredits(credits);
        user.setComptePMB(comptePMB);

        compteBancaire.setVirements(virements);
                user.setCompteBancaire(compteBancaire);

        when(comptePMBService.saveComptePMB(any(ComptePMB.class))).thenReturn(comptePMB);
        when(compteBancaireService.saveCompteBancaire(any(CompteBancaire.class))).thenReturn(compteBancaire);
        BigDecimal result = userService.getBalance(user);

        assertEquals(new BigDecimal("1090"), result);
    }
}