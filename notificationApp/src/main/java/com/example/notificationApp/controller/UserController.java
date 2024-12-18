package com.example.notificationApp.controller;

import com.example.notificationApp.entity.User;
import com.example.notificationApp.model.UserDTO;
import com.example.notificationApp.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    public UserController(UserService userService){
        this.userService=userService;
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user){
        return userService.createUser(user);
    }
    @PostMapping("/login")
    public String loginUser(@RequestBody User user){
        return userService.verify(user);
    }

    @GetMapping("/list")
    public List<User> getUsers(){
        return userService.getUsers();
    }
    @GetMapping("/{userId}")
    public Optional<User> getUser(@PathVariable int userId){
        return userService.getUser(userId);
    }
    @PutMapping("/update/{userId}")
    public User updateUser(@PathVariable int userId, @RequestBody UserDTO user){
        return userService.updateUser(user, userId);
    }
    @DeleteMapping("/delete/{userId}")
    public void deleteUser(@PathVariable int userId){
        userService.deleteUser(userId);
    }

}
