package com.picpaydesafio.picpaydesafio.controllers;

import com.picpaydesafio.picpaydesafio.domain.user.User;
import com.picpaydesafio.picpaydesafio.dtos.user.UserCreateDTO;
import com.picpaydesafio.picpaydesafio.dtos.user.UserReadDTO;
import com.picpaydesafio.picpaydesafio.dtos.user.UserUpdateDTO;
import com.picpaydesafio.picpaydesafio.services.UserService;
import jakarta.validation.Valid;
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
    public ResponseEntity<UserReadDTO> createUser(@RequestBody @Valid UserCreateDTO user){
        User newUser = this.service.createUser(user);
        return new ResponseEntity<>(UserReadDTO.fromEntity(newUser), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserReadDTO>> getUsers(){
        List<User> result = this.service.getAllUsers();
        List<UserReadDTO> users = result.stream().map(UserReadDTO::fromEntity).toList();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserReadDTO> getUserById(@PathVariable Long id) throws Exception {
        User user = this.service.findUserById(id);
        return  new ResponseEntity<>(UserReadDTO.fromEntity(user), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserReadDTO> updatedUser(
            @PathVariable Long id,
            @RequestBody @Valid UserUpdateDTO newData
    ) throws Exception {
        User user = this.service.updateUser(id, newData);
        return new ResponseEntity<>(UserReadDTO.fromEntity(user), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserReadDTO> deleteUser(@PathVariable Long id) throws Exception {
        User user = this.service.deleteUser(id);
        return new ResponseEntity<>(UserReadDTO.fromEntity(user), HttpStatus.OK);
    }
}
