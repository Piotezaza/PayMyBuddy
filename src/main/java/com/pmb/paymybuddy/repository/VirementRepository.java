package com.pmb.paymybuddy.repository;

import com.pmb.paymybuddy.model.Virement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VirementRepository extends JpaRepository<Virement, Integer> {
}
