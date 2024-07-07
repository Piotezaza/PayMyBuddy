package com.pmb.paymybuddy.repository;

import com.pmb.paymybuddy.model.ComptePMB;
import com.pmb.paymybuddy.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findByIssuer(ComptePMB comptePMB);
    List<Transaction> findByRecipient(ComptePMB comptePMB);

    Page<Transaction> findByIssuerOrRecipient(ComptePMB issuer, ComptePMB recipient, Pageable pageable);
}