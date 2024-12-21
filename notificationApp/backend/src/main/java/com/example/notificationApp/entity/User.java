package com.example.notificationApp.entity;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "tbl_users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "Username cannot be empty")
    private String username;
    @Email
    @NotEmpty(message = "Email cannot be empty")
    private String email;
    @NotEmpty(message = "Password cannot be empty")
    private String password;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User() {

    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
