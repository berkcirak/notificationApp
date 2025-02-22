package com.example.notificationApp.controller;

import com.example.notificationApp.entity.User;
import com.example.notificationApp.model.UserDTO;
import com.example.notificationApp.service.JWTService;
import com.example.notificationApp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    private JWTService jwtService;
    public UserController(UserService userService, JWTService jwtService){
        this.jwtService=jwtService;
        this.userService=userService;
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody User user){
       User createdUser = userService.createUser(user);
       return jwtService.generateToken(createdUser.getUsername());

    }
    @PostMapping("/login")
    public String loginUser(@RequestBody User user){
        return userService.verify(user);
    }

    @GetMapping("/profile")
    public User userProfile(){
        return userService.getUserPrincipal();
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
    public ResponseEntity<Map<String, String>> updateUser(@PathVariable int userId, @RequestBody UserDTO user) {
        User updatedUser = userService.updateUser(user, userId);

        // Eğer kullanıcı adını güncellemişse yeni token üret
        String newToken = jwtService.generateToken(updatedUser.getUsername());

        // Yeni token'ı frontend'e dön
        Map<String, String> response = new HashMap<>();
        response.put("token", newToken);
        response.put("message", "Kullanıcı güncellendi.");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{userId}")
    public void deleteUser(@PathVariable int userId){
        userService.deleteUser(userId);
    }

}
