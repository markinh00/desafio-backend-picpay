package com.picpaydesafio.picpaydesafio.controllers;


import com.picpaydesafio.picpaydesafio.domain.user.User;
import com.picpaydesafio.picpaydesafio.dtos.user.UserDTO;
import com.picpaydesafio.picpaydesafio.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService service;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDTO user){
        User newUser = service.createUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers(){
        List<User> users = this.service.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
