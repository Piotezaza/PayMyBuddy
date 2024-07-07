package com.pmb.paymybuddy.service;

import com.pmb.paymybuddy.model.ComptePMB;
import com.pmb.paymybuddy.model.Transaction;
import com.pmb.paymybuddy.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    private TaxeService taxeService;

    @Autowired
    public void setTaxeService(TaxeService taxeService) {
        this.taxeService = taxeService;
    }

    public List<Transaction> getTransaction() {
        return transactionRepository.findAll();
    }

    public Page<Transaction> getAllTransactionsForAccountPageable(ComptePMB comptePMB, Pageable pageable) {
        return transactionRepository.findByIssuerOrRecipient(comptePMB, comptePMB, pageable);
    }

    public void addTransaction(Transaction transaction) {
        transaction.setFrais(taxeService.addTaxe(transaction));
        saveTransaction(transaction);
    }

    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }
}
