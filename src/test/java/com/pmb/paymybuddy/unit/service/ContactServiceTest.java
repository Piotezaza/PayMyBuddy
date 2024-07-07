package com.pmb.paymybuddy.unit.service;

import com.pmb.paymybuddy.model.Contact;
import com.pmb.paymybuddy.model.User;
import com.pmb.paymybuddy.repository.ContactRepository;
import com.pmb.paymybuddy.service.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class ContactServiceTest {

    @InjectMocks
    ContactService contactService;

    @Mock
    ContactRepository contactRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Should return all contacts")
    public void getContact() {
        Contact contact = new Contact();
        when(contactRepository.findAll()).thenReturn(Arrays.asList(contact));

        List<Contact> contacts = contactService.getContact();

        assertEquals(1, contacts.size());
        verify(contactRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return contact by id")
    public void getContactById() {
        Contact contact = new Contact();
        contact.setId(1);
        when(contactRepository.findById(1)).thenReturn(Optional.of(contact));

        Contact result = contactService.getContactById(1);

        assertEquals(contact, result);
    }

    @Test
    @DisplayName("Should return null when contact not found by id")
    public void getContactByIdNotFound() {
        when(contactRepository.findById(1)).thenReturn(Optional.empty());

        Contact result = contactService.getContactById(1);

        assertNull(result);
    }

    @Test
    @DisplayName("Should return contacts by user")
    public void getContactsByUser() {
        User user = new User();
        Contact contact = new Contact();
        when(contactRepository.findByUser(user)).thenReturn(Arrays.asList(contact));

        List<Contact> contacts = contactService.getContactsByUser(user);

        assertEquals(1, contacts.size());
        verify(contactRepository, times(1)).findByUser(user);
    }

    @Test
    @DisplayName("Should return contact by user and contact")
    public void findByUserAndContact() {
        User user = new User();
        User contactUser = new User();
        Contact contact = new Contact();
        when(contactRepository.findByUserAndContact(user, contactUser)).thenReturn(contact);

        Contact result = contactService.findByUserAndContact(user, contactUser);

        assertEquals(contact, result);
        verify(contactRepository, times(1)).findByUserAndContact(user, contactUser);
    }

    @Test
    @DisplayName("Should add contact successfully")
    public void addContact() {
        Contact contact = new Contact();
        when(contactRepository.save(contact)).thenReturn(contact);

        Contact result = contactService.addContact(contact);

        assertEquals(contact, result);
        verify(contactRepository, times(1)).save(contact);
    }

    @Test
    @DisplayName("Should delete contact by id")
    public void deleteContactById() {
        Contact contact = new Contact();
        contact.setId(1);
        when(contactRepository.findById(1)).thenReturn(Optional.of(contact));

        contactService.deleteContactById(1);

        verify(contactRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Should not delete contact when id not found")
    public void deleteContactByIdNotFound() {
        when(contactRepository.findById(1)).thenReturn(Optional.empty());

        contactService.deleteContactById(1);

        verify(contactRepository, times(0)).deleteById(1);
    }
}
