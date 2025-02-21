package com.example.notificationApp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String link;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private User user;

    public Product(){

    }

    public Product(String title, String link, User user) {
        this.title = title;
        this.link = link;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
