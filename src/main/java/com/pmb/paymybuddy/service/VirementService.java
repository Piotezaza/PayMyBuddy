package com.pmb.paymybuddy.service;

import com.pmb.paymybuddy.model.CompteBancaire;
import com.pmb.paymybuddy.model.Virement;
import com.pmb.paymybuddy.repository.VirementRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VirementService {
    private final VirementRepository virementRepository;

    public List<Virement> getVirement(){
        return virementRepository.findAll();
    }

    public Virement getVirementById(Integer id){
        Optional<Virement> optionalVirement = virementRepository.findById(id);
        return optionalVirement.orElse(null);
    }

    public List<Virement> getVirementsSent(CompteBancaire compteBancaire) {
        return virementRepository.findByTypeAndCompteBancaire("OUT", compteBancaire);
    }

    public List<Virement> getVirementsReceived(CompteBancaire compteBancaire) {
        return virementRepository.findByTypeAndCompteBancaire("IN", compteBancaire);
    }

    public Virement addVirement(Virement virement){
        return saveVirement(virement);
    }

    public Virement saveVirement(Virement virement){
        return virementRepository.save(virement);
    }

    public void deleteVirementById(Integer id){
        Optional<Virement> optionalVirement = virementRepository.findById(id);
        if (optionalVirement.isPresent()){
            virementRepository.deleteById(id);
        }
    }

    public Page<Virement> getAllVirementsForAccountPageable(CompteBancaire compteBancaire, Pageable pageable) {
        return virementRepository.findByCompteBancaire(compteBancaire, pageable);
    }
}
