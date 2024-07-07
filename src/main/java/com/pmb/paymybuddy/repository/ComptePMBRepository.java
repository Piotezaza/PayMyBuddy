package com.pmb.paymybuddy.repository;

import com.pmb.paymybuddy.model.ComptePMB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComptePMBRepository extends JpaRepository<ComptePMB, Integer> {
}