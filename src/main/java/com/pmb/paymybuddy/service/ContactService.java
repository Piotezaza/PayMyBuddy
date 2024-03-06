package com.pmb.paymybuddy.service;

import com.pmb.paymybuddy.model.Contact;
import com.pmb.paymybuddy.repository.ContactRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ContactService {
    private final ContactRepository contactRepository;

    public List<Contact> getContact(){
        return contactRepository.findAll();
    }

    public Contact getContactById(Integer id){
        Optional<Contact> optionalContact = contactRepository.findById(id);
        return optionalContact.orElse(null);
    }

    public Contact addContact(Contact contact){
        return saveContact(contact);
    }

    public Contact saveContact(Contact contact){
        return contactRepository.save(contact);
    }

    public void deleteContactById(Integer id){
        Optional<Contact> optionalContact = contactRepository.findById(id);
        if (optionalContact.isPresent()){
            contactRepository.deleteById(id);
        }
    }
}
