package com.pmb.paymybuddy.service;

import com.pmb.paymybuddy.model.Transaction;
import com.pmb.paymybuddy.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public List<Transaction> getTransaction(){
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(Integer id){
        Optional<Transaction> optionalTransaction = transactionRepository.findById(id);
        return optionalTransaction.orElse(null);
    }

    public Transaction addTransaction(Transaction transaction){
        return saveTransaction(transaction);
    }

    public Transaction saveTransaction(Transaction transaction){
        return transactionRepository.save(transaction);
    }

    public void deleteTransactionById(Integer id){
        Optional<Transaction> optionalTransaction = transactionRepository.findById(id);
        if (optionalTransaction.isPresent()){
            transactionRepository.deleteById(id);
        }
    }
}
