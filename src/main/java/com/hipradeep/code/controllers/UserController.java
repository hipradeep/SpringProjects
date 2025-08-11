package com.hipradeep.code.controllers;


import com.hipradeep.code.entities.User;
import com.hipradeep.code.exceptions.ResourceNotFoundException;
import com.hipradeep.code.repositories.UserRepository;
import com.hipradeep.code.services.UserProfileService;
import com.hipradeep.code.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    private Logger logger = LoggerFactory.getLogger(UserController.class);



    @Autowired
    private UserProfileService userProfileService;



    //CREATE
    //@RequestBody to get all data through JSON (in JSON format)
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        User user1 = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user1);
    }

    //GET All Users
    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> allUsers = userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    //GET Single User
    @GetMapping("/{userId}")
    public ResponseEntity<User> getSingleUser(@PathVariable String userId){
        User user = userService.getUser(userId);

        return ResponseEntity.ok(user);
    }

    //UPDATE User
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable String userId, @RequestBody User user){
        User existingUser = this.userRepository.findById(userId).orElseThrow(()->
                new ResourceNotFoundException("User not found with user id: "+userId));

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setAbout(user.getAbout());

        User savedUser = this.userRepository.save(existingUser);

        return ResponseEntity.ok(savedUser);
    }

    //Delete User
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable String userId){

        User user = this.userRepository.findById(userId).orElseThrow(()->
                new ResourceNotFoundException("User not found with id: "+userId));

        this.userRepository.delete(user);
        System.out.println("User deleted");
    }



}
