package com.pmb.paymybuddy.unit.service;

import com.pmb.paymybuddy.model.ComptePMB;
import com.pmb.paymybuddy.model.Transaction;
import com.pmb.paymybuddy.repository.TransactionRepository;
import com.pmb.paymybuddy.service.TaxeService;
import com.pmb.paymybuddy.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @InjectMocks
    TransactionService transactionService;

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    TaxeService taxeService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Should return all transactions")
    public void getTransaction() {
        Transaction transaction = new Transaction();
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(transaction));

        List<Transaction> transactions = transactionService.getTransaction();

        assertEquals(1, transactions.size());
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return all transactions for account pageable")
    public void getAllTransactionsForAccountPageable() {
        ComptePMB comptePMB = new ComptePMB();
        PageRequest pageable = PageRequest.of(0, 5);
        Page<Transaction> page = mock(Page.class);
        when(transactionRepository.findByIssuerOrRecipient(comptePMB, comptePMB, pageable)).thenReturn(page);

        Page<Transaction> result = transactionService.getAllTransactionsForAccountPageable(comptePMB, pageable);

        assertEquals(page, result);
        verify(transactionRepository, times(1)).findByIssuerOrRecipient(comptePMB, comptePMB, pageable);
    }

    @Test
    @DisplayName("Should add transaction with taxe")
    public void addTransaction() {
        Transaction transaction = new Transaction();
        transaction.setMontant(new BigDecimal("1000"));
        when(taxeService.addTaxe(transaction)).thenReturn(new BigDecimal("50"));

        transactionService.addTransaction(transaction);

        assertEquals(new BigDecimal("50"), transaction.getFrais());
        verify(taxeService, times(1)).addTaxe(transaction);
        verify(transactionRepository, times(1)).save(transaction);
    }
}
