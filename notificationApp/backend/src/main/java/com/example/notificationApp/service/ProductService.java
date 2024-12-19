package com.example.notificationApp.service;

import com.example.notificationApp.entity.Product;
import com.example.notificationApp.entity.User;
import com.example.notificationApp.model.ProductDTO;
import com.example.notificationApp.repository.ProductRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private ProductRepository productRepository;
    private UserService userService;
    public ProductService(ProductRepository productRepository, UserService userService){
        this.productRepository=productRepository;
        this.userService=userService;
    }
    public Product addProduct(Product product){
        User user = userService.getAuthenticatedUser();
        product.setUser(user);
        return productRepository.save(product);
    }
    public List<Product> getProductsByUserId(){
        User user = userService.getAuthenticatedUser();
        return productRepository.findAllByUserId(user.getId());
    }
    public Product getProductById(int productId){
        return productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found")); //user'ın product table'ı icinden cekilecek düzenlenebilir
    }
    public Product updateProduct(int productId, ProductDTO productDTO){
        User user = userService.getAuthenticatedUser();
        Product product = productRepository.findById(productId).orElseThrow(()->new RuntimeException("Product not found"));
        if (user.getId() != product.getUser().getId()){
            throw new RuntimeException("You are not authorized for update this product");
        }
        if (productDTO.getTitle() != null){
            product.setTitle(productDTO.getTitle());
        }
        if (productDTO.getLink() != null){
            product.setLink(productDTO.getLink());
        }
        return productRepository.save(product);
    }
    public void deleteProduct(int productId){
        User user = userService.getAuthenticatedUser();
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        if (user.getId() != product.getUser().getId()){
            throw new RuntimeException("You are not authorized for delete this product");
        }
        productRepository.deleteById(productId);
    }



}
