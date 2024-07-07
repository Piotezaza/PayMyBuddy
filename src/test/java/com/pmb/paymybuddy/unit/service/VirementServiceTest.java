package com.pmb.paymybuddy.unit.service;

import com.pmb.paymybuddy.model.CompteBancaire;
import com.pmb.paymybuddy.model.Virement;
import com.pmb.paymybuddy.repository.VirementRepository;
import com.pmb.paymybuddy.service.VirementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class VirementServiceTest {

    @InjectMocks
    VirementService virementService;

    @Mock
    VirementRepository virementRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Should return all virements")
    public void getVirement() {
        Virement virement = new Virement();
        when(virementRepository.findAll()).thenReturn(Arrays.asList(virement));

        List<Virement> virements = virementService.getVirement();

        assertEquals(1, virements.size());
        verify(virementRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return virement by id")
    public void getVirementById() {
        Virement virement = new Virement();
        virement.setId(1);
        when(virementRepository.findById(1)).thenReturn(Optional.of(virement));

        Virement result = virementService.getVirementById(1);

        assertEquals(virement, result);
    }

    @Test
    @DisplayName("Should return null when virement not found by id")
    public void getVirementByIdNotFound() {
        when(virementRepository.findById(1)).thenReturn(Optional.empty());

        Virement result = virementService.getVirementById(1);

        assertNull(result);
    }

    @Test
    @DisplayName("Should return virements sent by compteBancaire")
    public void getVirementsSent() {
        CompteBancaire compteBancaire = new CompteBancaire();
        Virement virement = new Virement();
        when(virementRepository.findByTypeAndCompteBancaire("OUT", compteBancaire)).thenReturn(Arrays.asList(virement));

        List<Virement> virements = virementService.getVirementsSent(compteBancaire);

        assertEquals(1, virements.size());
        verify(virementRepository, times(1)).findByTypeAndCompteBancaire("OUT", compteBancaire);
    }

    @Test
    @DisplayName("Should return virements received by compteBancaire")
    public void getVirementsReceived() {
        CompteBancaire compteBancaire = new CompteBancaire();
        Virement virement = new Virement();
        when(virementRepository.findByTypeAndCompteBancaire("IN", compteBancaire)).thenReturn(Arrays.asList(virement));

        List<Virement> virements = virementService.getVirementsReceived(compteBancaire);

        assertEquals(1, virements.size());
        verify(virementRepository, times(1)).findByTypeAndCompteBancaire("IN", compteBancaire);
    }

    @Test
    @DisplayName("Should add virement successfully")
    public void addVirement() {
        Virement virement = new Virement();
        when(virementRepository.save(virement)).thenReturn(virement);

        Virement result = virementService.addVirement(virement);

        assertEquals(virement, result);
        verify(virementRepository, times(1)).save(virement);
    }

    @Test
    @DisplayName("Should delete virement by id")
    public void deleteVirementById() {
        Virement virement = new Virement();
        virement.setId(1);
        when(virementRepository.findById(1)).thenReturn(Optional.of(virement));

        virementService.deleteVirementById(1);

        verify(virementRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Should not delete virement when id not found")
    public void deleteVirementByIdNotFound() {
        when(virementRepository.findById(1)).thenReturn(Optional.empty());

        virementService.deleteVirementById(1);

        verify(virementRepository, times(0)).deleteById(1);
    }

    @Test
    @DisplayName("Should return all virements for account pageable")
    public void getAllVirementsForAccountPageable() {
        CompteBancaire compteBancaire = new CompteBancaire();
        PageRequest pageable = PageRequest.of(0, 5);
        Page<Virement> page = mock(Page.class);
        when(virementRepository.findByCompteBancaire(compteBancaire, pageable)).thenReturn(page);

        Page<Virement> result = virementService.getAllVirementsForAccountPageable(compteBancaire, pageable);

        assertEquals(page, result);
        verify(virementRepository, times(1)).findByCompteBancaire(compteBancaire, pageable);
    }
}