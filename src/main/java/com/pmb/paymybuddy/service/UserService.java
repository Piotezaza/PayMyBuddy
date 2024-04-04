package com.pmb.paymybuddy.service;

import com.pmb.paymybuddy.model.CompteBancaire;
import com.pmb.paymybuddy.model.User;
import com.pmb.paymybuddy.model.ComptePMB;
import com.pmb.paymybuddy.model.User;
import com.pmb.paymybuddy.repository.ComptePMBRepository;
import com.pmb.paymybuddy.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ComptePMBService comptePMBService;
    private PasswordEncoder passwordEncoder;

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public User getUserById(Integer id){
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    public User getUserByEmail(String email){
        User user = userRepository.findByEmail(email);
        return user;
    }

    private boolean emailExists(String email) {
        return getUserByEmail(email) != null;
    }

    public void addUser(User user) throws Exception {
        if (emailExists(user.getEmail())){
            throw new Exception("There is already a user with this email");
        }

        ComptePMB comptePMB = new ComptePMB();
        ComptePMB createdComptePMB = comptePMBService.saveComptePMB(comptePMB);

        User newUser = new User();
        newUser.setNom(user.getNom());
        newUser.setPrenom(user.getPrenom());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setComptePMB(createdComptePMB);
        saveUser(newUser);
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public void deleteUserById(Integer id){
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()){
            userRepository.deleteById(id);
        }
    }

}
