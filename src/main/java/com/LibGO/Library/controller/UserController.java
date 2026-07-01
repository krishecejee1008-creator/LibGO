package com.LibGO.Library.controller;

import com.LibGO.Library.model.User;
import com.LibGO.Library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/searchUserByEmail")
    public Optional<User> getByEmail(@RequestParam String email){

        return userService.getUserByEmail(email);

    }

    @GetMapping("/searchUserByEnrollmentId")
    public Optional<User> getByEnrollmentId(@RequestParam Long enrollmentId){

        return userService.getUserByEnrollmentID(enrollmentId);

    }

    @GetMapping("/searchUserByName")
    public Optional<User> getByName(@RequestParam String name){

        return userService.getUserByName(name);

    }

    @PostMapping("/register")
    public User registerNewUser(@RequestBody User user){

        return userService.registerUser(user);

    }

}
