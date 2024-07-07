package com.pmb.paymybuddy.service;

import com.pmb.paymybuddy.model.CompteBancaire;
import com.pmb.paymybuddy.repository.CompteBancaireRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CompteBancaireService {

    private final CompteBancaireRepository compteBancaireRepository;

    private final VirementService virementService;

    public List<CompteBancaire> getCompteBancaires() {
        return compteBancaireRepository.findAll();
    }

    public CompteBancaire getCompteBancaireById(Integer id) {
        Optional<CompteBancaire> optionalCompteBancaire = compteBancaireRepository.findById(id);
        return optionalCompteBancaire.orElse(null);
    }

    public CompteBancaire getCompteBancaireByIban(String iban) {
        Optional<CompteBancaire> optionalCompteBancaire = compteBancaireRepository.findByIban(iban);
        return optionalCompteBancaire.orElse(null);
    }

    @Transactional
    public CompteBancaire addCompteBancaire(CompteBancaire compteBancaire) {
        return saveCompteBancaire(compteBancaire);
    }

    public CompteBancaire saveCompteBancaire(CompteBancaire compteBancaire) {
        return compteBancaireRepository.save(compteBancaire);
    }
}
