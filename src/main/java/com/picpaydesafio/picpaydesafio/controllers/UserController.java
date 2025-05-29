package com.picpaydesafio.picpaydesafio.controllers;

import com.picpaydesafio.picpaydesafio.domain.user.User;
import com.picpaydesafio.picpaydesafio.dtos.user.UserCreateDTO;
import com.picpaydesafio.picpaydesafio.dtos.user.UserReadDTO;
import com.picpaydesafio.picpaydesafio.dtos.user.UserUpdateDTO;
import com.picpaydesafio.picpaydesafio.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Users", description = "Endpoints for managing users")
@RestController()
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService service;

    @Operation(
            summary = "Create a new user",
            description = "Creates a new user with the provided data and returns the created user.")
    @PostMapping
    public ResponseEntity<UserReadDTO> createUser(@RequestBody @Valid UserCreateDTO user){
        User newUser = this.service.createUser(user);
        return new ResponseEntity<>(UserReadDTO.fromEntity(newUser), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all users",
            description = "Retrieves a list of all registered users.")
    @GetMapping
    public ResponseEntity<List<UserReadDTO>> getUsers(){
        List<User> result = this.service.getAllUsers();
        List<UserReadDTO> users = result.stream().map(UserReadDTO::fromEntity).toList();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Operation(
            summary = "Get a user by ID",
            description = "Retrieves a user by their unique ID.")
    @GetMapping("/{id}")
    public ResponseEntity<UserReadDTO> getUserById(@PathVariable Long id) throws Exception {
        User user = this.service.findUserById(id);
        return  new ResponseEntity<>(UserReadDTO.fromEntity(user), HttpStatus.OK);
    }

    @Operation(
            summary = "Update a user",
            description = "Updates the information of an existing user identified by ID.")
    @PutMapping("/{id}")
    public ResponseEntity<UserReadDTO> updatedUser(
            @PathVariable Long id,
            @RequestBody @Valid UserUpdateDTO newData
    ) throws Exception {
        User user = this.service.updateUser(id, newData);
        return new ResponseEntity<>(UserReadDTO.fromEntity(user), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete a user",
            description = "Deletes the user identified by the given ID and returns the deleted user data.")
    @DeleteMapping("/{id}")
    public ResponseEntity<UserReadDTO> deleteUser(@PathVariable Long id) throws Exception {
        User user = this.service.deleteUser(id);
        return new ResponseEntity<>(UserReadDTO.fromEntity(user), HttpStatus.OK);
    }
}
