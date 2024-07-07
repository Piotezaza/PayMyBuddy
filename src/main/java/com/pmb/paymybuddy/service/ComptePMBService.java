package com.pmb.paymybuddy.service;

import com.pmb.paymybuddy.model.ComptePMB;
import com.pmb.paymybuddy.repository.ComptePMBRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ComptePMBService {

    private final TransactionService transactionService;

    private final ComptePMBRepository comptePMBRepository;

    public List<ComptePMB> getComptesPMB() {
        return comptePMBRepository.findAll();
    }

    public ComptePMB getComptePMBById(Integer id) {
        Optional<ComptePMB> optionalComptePMB = comptePMBRepository.findById(id);
        return optionalComptePMB.orElse(null);
    }

    @Transactional
    public ComptePMB addComptePMB(ComptePMB comptePMB) {
        return saveComptePMB(comptePMB);
    }

    public ComptePMB saveComptePMB(ComptePMB comptePMB) {
        return comptePMBRepository.save(comptePMB);
    }

    public void deleteComptePMBById(Integer id) {
        Optional<ComptePMB> optionalComptePMB = comptePMBRepository.findById(id);
        if (optionalComptePMB.isPresent()) {
            comptePMBRepository.deleteById(id);
        }
    }
}
