package com.example.notificationApp.service;

import com.example.notificationApp.entity.User;
import com.example.notificationApp.entity.UserDTO;
import com.example.notificationApp.entity.UserPrincipal;
import com.example.notificationApp.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private AuthenticationManager authenticationManager;
    private JWTService jwtService;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager, JWTService jwtService){
        this.userRepository=userRepository;
        this.bCryptPasswordEncoder=bCryptPasswordEncoder;
        this.authenticationManager=authenticationManager;
        this.jwtService=jwtService;
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

    public String verify(User user){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (authentication.isAuthenticated()){
            return jwtService.generateToken(user.getUsername());
        }
        return "fail";
    }
    public User getAuthenticatedUser(User user) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails){
            username=((UserDetails) principal).getUsername();
        }else {
            username = principal.toString();
        }
        return userRepository.findByUsername(username);
    }
}
