package com.picpaydesafio.picpaydesafio.services;

import com.picpaydesafio.picpaydesafio.domain.user.User;
import com.picpaydesafio.picpaydesafio.domain.user.UserType;
import com.picpaydesafio.picpaydesafio.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public void saveUser(User user){
        this.repository.save(user);
    }

    public User findUserById(Long id) throws Exception {
        return this.repository.findUserById(id).orElseThrow(() -> new Exception("User not found"));
    }

    public void validateTransaction(User sender, BigDecimal amount) throws Exception {
        if (sender.getUserType() == UserType.MERCHANT) {
            throw new Exception("User of type MERCHANT is not allowed to make transactions");
        }

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new Exception("User does not have enough balance");
        }
    }

}
