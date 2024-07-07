package com.pmb.paymybuddy.repository;

import com.pmb.paymybuddy.model.CompteBancaire;
import com.pmb.paymybuddy.model.Virement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VirementRepository extends JpaRepository<Virement, Integer> {
    List<Virement> findByTypeAndCompteBancaire(String type, CompteBancaire compteBancaire);

    Page<Virement> findByCompteBancaire(CompteBancaire compteBancaire, Pageable pageable);
}
