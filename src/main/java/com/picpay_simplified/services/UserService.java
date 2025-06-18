package com.picpay_simplified.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.picpay_simplified.domain.user.User;
import com.picpay_simplified.domain.user.UserType;
import com.picpay_simplified.dtos.UserDTO;
import com.picpay_simplified.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public void validateTransaction(User payer, BigDecimal amount) throws Exception{
        if(payer.getUserType() == UserType.MERCHANT){
            throw new Exception("Logistas não realizam transações.");
        }
        if(payer.getBalance().compareTo(amount) < 0){
            throw new Exception("Saldo insuficiente para realizar transação.");
        }
    }

    public User findUserById(Long id) throws Exception{
        return this.repository.findUserById(id).orElseThrow(() -> new Exception("Usuário não encontrado."));
    }

    public void saveUser(User user){
        this.repository.save(user);
    }

    public User createUser(UserDTO user){
        User newUser = new User(user);
        this.saveUser(newUser);
        return newUser;
    }

    public List<User> getAllUsers(){
        return this.repository.findAll(); 
    }
}