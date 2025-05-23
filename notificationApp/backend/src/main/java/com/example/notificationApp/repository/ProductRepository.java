package com.example.notificationApp.repository;

import com.example.notificationApp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findAllByUserId(int id);

    List<Product> findProductByOriginalPriceNot(String originalPrice);
}
