package com.pmb.paymybuddy.unit.service;

import com.pmb.paymybuddy.model.Transaction;
import com.pmb.paymybuddy.service.TaxeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaxeServiceTest {

    private TaxeService taxeService;

    @BeforeEach
    public void setUp() {
        taxeService = new TaxeService();
    }

    @Test
    @DisplayName("Should calculate taxe correctly for positive transaction amount")
    public void addTaxePositiveAmount() {
        Transaction transaction = new Transaction();
        transaction.setMontant(new BigDecimal("1000"));

        BigDecimal taxe = taxeService.addTaxe(transaction);

        assertEquals(new BigDecimal("50.00"), taxe);
    }

    @Test
    @DisplayName("Should calculate taxe as zero for zero transaction amount")
    public void addTaxeZeroAmount() {
        Transaction transaction = new Transaction();
        transaction.setMontant(BigDecimal.ZERO);

        BigDecimal taxe = taxeService.addTaxe(transaction).stripTrailingZeros();

        assertEquals(BigDecimal.ZERO, taxe);
    }

    @Test
    @DisplayName("Should calculate taxe correctly for negative transaction amount")
    public void addTaxeNegativeAmount() {
        Transaction transaction = new Transaction();
        transaction.setMontant(new BigDecimal("-1000"));

        BigDecimal taxe = taxeService.addTaxe(transaction);

        assertEquals(new BigDecimal("-50.00"), taxe);
    }
}