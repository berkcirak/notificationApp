package com.example.notificationApp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_recommended_products")
public class RecommendedProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "original_product_id")
    private Product originalProduct;

    @ManyToOne
    @JoinColumn(name = "recommended_product_id")
    private Product recommendedProduct;

    public RecommendedProduct(){}

    public RecommendedProduct(Product originalProduct, Product recommendedProduct) {
        this.originalProduct = originalProduct;
        this.recommendedProduct = recommendedProduct;
    }

    public int getId() {
        return id;
    }

    public Product getOriginalProduct() {
        return originalProduct;
    }

    public Product getRecommendedProduct() {
        return recommendedProduct;
    }
}
