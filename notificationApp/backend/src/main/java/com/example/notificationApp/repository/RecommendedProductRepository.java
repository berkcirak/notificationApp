package com.example.notificationApp.repository;

import com.example.notificationApp.entity.Product;
import com.example.notificationApp.entity.RecommendedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendedProductRepository extends JpaRepository<RecommendedProduct, Integer> {

    List<RecommendedProduct> findByOriginalProduct(Product originalProduct);


}
