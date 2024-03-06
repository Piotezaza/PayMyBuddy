package com.pmb.paymybuddy.service;

import com.pmb.paymybuddy.model.ComptePMB;
import com.pmb.paymybuddy.model.User;
import com.pmb.paymybuddy.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public User getUserById(Integer id){
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.orElse(null);
    }

    public User addUser(User user){
        ComptePMB comptePMB = new ComptePMB();

        // TODO : lorsqu'il y aura le mot de passe, penser à l'encoder ici
        user.setCompte_pmb(comptePMB);

        return saveUser(user);
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
