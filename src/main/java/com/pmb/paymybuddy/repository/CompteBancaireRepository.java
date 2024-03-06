package com.pmb.paymybuddy.repository;

import com.pmb.paymybuddy.model.CompteBancaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompteBancaireRepository extends JpaRepository<CompteBancaire, Integer> {

    Optional<CompteBancaire> findByIban(String iban);
}