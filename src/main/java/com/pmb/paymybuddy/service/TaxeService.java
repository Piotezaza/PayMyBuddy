package com.pmb.paymybuddy.service;

import com.pmb.paymybuddy.model.Transaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TaxeService {
    private final double taxe = 0.05;

    public BigDecimal addTaxe(Transaction transaction) {
        BigDecimal amount = transaction.getMontant();
        return amount.multiply(BigDecimal.valueOf(taxe));
    }
}
