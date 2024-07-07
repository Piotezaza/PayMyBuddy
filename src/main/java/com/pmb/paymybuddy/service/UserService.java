package com.pmb.paymybuddy.service;

import com.pmb.paymybuddy.model.CompteBancaire;
import com.pmb.paymybuddy.model.ComptePMB;
import com.pmb.paymybuddy.model.User;
import com.pmb.paymybuddy.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ComptePMBService comptePMBService;
    private final CompteBancaireService compteBancaireService;
    private PasswordEncoder passwordEncoder;

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public User getUserById(Integer id){
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    private boolean emailExists(String email) {
        return getUserByEmail(email) != null;
    }

    @Transactional
    public void addUser(User user) throws Exception {
        if (emailExists(user.getEmail())){
            throw new Exception("There is already a user with this email");
        }

        ComptePMB comptePMB = new ComptePMB();
        ComptePMB createdComptePMB = comptePMBService.saveComptePMB(comptePMB);

        CompteBancaire compteBancaire = new CompteBancaire();
        CompteBancaire createdCompteBancaire = compteBancaireService.saveCompteBancaire(compteBancaire);

        User newUser = new User();
        newUser.setNom(user.getNom());
        newUser.setPrenom(user.getPrenom());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setComptePMB(createdComptePMB);
        newUser.setCompteBancaire(createdCompteBancaire);
        saveUser(newUser);
    }

    @Transactional
    public User saveUser(User user){
        return userRepository.save(user);
    }

    public void deleteUserById(Integer id){
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()){
            userRepository.deleteById(id);
        }
    }

    public BigDecimal getBalance(User user){
        BigDecimal comptePMBSolde = user.getComptePMB().getBalance();
        BigDecimal compteBancaireSolde = user.getCompteBancaire().getBalance();
        return comptePMBSolde.add(compteBancaireSolde);
    }
}
