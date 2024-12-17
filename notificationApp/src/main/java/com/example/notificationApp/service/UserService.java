package com.example.notificationApp.service;

import com.example.notificationApp.entity.User;
import com.example.notificationApp.entity.UserDTO;
import com.example.notificationApp.entity.UserPrincipal;
import com.example.notificationApp.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository=userRepository;
        this.bCryptPasswordEncoder=bCryptPasswordEncoder;
    }

    public User createUser(User user){
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    public List<User> getUsers(){
        return userRepository.findAll();
    }
    public Optional<User> getUser(int userId){
        return userRepository.findById(userId);
    }
    public User updateUser(UserDTO currentUser, int userId){
        User userOptional=userRepository.findById(userId).orElseThrow(() -> new RuntimeException("user not found"));
        if (currentUser.getUsername() != null){
            userOptional.setUsername(currentUser.getUsername());
        }
        if (currentUser.email != null){
            userOptional.setEmail(currentUser.getEmail());
        }
        if (currentUser.getPassword() != null){
            userOptional.setPassword(currentUser.getPassword());
        }
        return userRepository.save(userOptional);
    }
    public void deleteUser(int userId){
        userRepository.deleteById(userId);
    }

}
