package com.picpaydesafio.picpaydesafio.services;

import com.picpaydesafio.picpaydesafio.domain.user.User;
import com.picpaydesafio.picpaydesafio.domain.user.UserType;
import com.picpaydesafio.picpaydesafio.dtos.user.UserCreateDTO;
import com.picpaydesafio.picpaydesafio.dtos.user.UserUpdateDTO;
import com.picpaydesafio.picpaydesafio.repositories.UserRepository;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    private boolean isEmailValid(User user) {
        Optional<User> foundUser = this.repository.findUserByEmail(user.getEmail());
        if (foundUser.isPresent() && foundUser.get().getId() != user.getId()) {
            throw new DataIntegrityViolationException("Document already in use");
        }
        return true;
    }

    private boolean isDocumentValid(User user) {
        Optional<User> foundUser = this.repository.findUserByDocument(user.getDocument());
        if (foundUser.isPresent() && foundUser.get().getId() != user.getId()) {
            throw new DataIntegrityViolationException("Document already in use");
        }
        return true;
    }

    public void saveUser(User user) {
        if (isDocumentValid(user) && isEmailValid(user)) {
            this.repository.save(user);
        }
    }

    public User createUser(UserCreateDTO data) {
        User newUser = new User(data);
        this.saveUser(newUser);
        return newUser;
    }

    public List<User> getAllUsers() {
        return this.repository.findAll();
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

    public User updateUser(Long id, UserUpdateDTO newUserData) throws Exception {
        Optional<User> optionalUser = this.repository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new Exception("User not found");
        }

        User user = optionalUser.get();

        BeanWrapper src = new BeanWrapperImpl(newUserData);
        BeanWrapper target = new BeanWrapperImpl(user);

        Arrays.stream(UserUpdateDTO.class.getDeclaredFields())
                .map(Field::getName)
                .filter(propertyName -> src.getPropertyValue(propertyName) != null)
                .forEach(propertyName -> {
                    Object value = src.getPropertyValue(propertyName);
                    target.setPropertyValue(propertyName, value);
                });

        this.saveUser(user);

        return user;
    }

    public User deleteUser(Long id) throws Exception {
        Optional<User> optionalUser = this.repository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new Exception("User not found");
        }
        User user = optionalUser.get();
        this.repository.deleteById(id);
        return user;
    }

}
