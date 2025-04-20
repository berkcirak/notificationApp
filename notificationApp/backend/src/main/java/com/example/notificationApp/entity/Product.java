package com.example.notificationApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tbl_products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(columnDefinition = "TEXT")
    private String link;

    private String productName;

    private String productPrice;
    private String originalPrice;
    @Column(columnDefinition = "TEXT")
    private String imageUrl;
    private LocalDateTime scrapedAt;

    private String inStock = "true";

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "originalProduct", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecommendedProduct> recommendedAsOriginal;

    @OneToMany(mappedBy = "recommendedProduct", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecommendedProduct> recommendedAsRecommended;


    public Product(){

    }

    public Product(String link, User user, String inStock, String imageUrl) {
        this.link = link;
        this.user = user;
        this.imageUrl = imageUrl;
        this.inStock = inStock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getInStock() {
        return inStock;
    }

    public void setInStock(String inStock) {
        this.inStock = inStock;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public LocalDateTime getScrapedAt() {
        return scrapedAt;
    }

    public void setScrapedAt(LocalDateTime scrapedAt) {
        this.scrapedAt = scrapedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
