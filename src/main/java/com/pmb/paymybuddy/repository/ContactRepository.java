package com.pmb.paymybuddy.repository;

import com.pmb.paymybuddy.model.Contact;
import com.pmb.paymybuddy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {
    List<Contact> findByUser(User user);

    Contact findByUserAndContact(User user, User contact);
}
