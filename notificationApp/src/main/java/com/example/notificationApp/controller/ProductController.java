package com.example.notificationApp.controller;

import com.example.notificationApp.entity.Product;
import com.example.notificationApp.model.ProductDTO;
import com.example.notificationApp.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private ProductService productService;
    public ProductController(ProductService productService){
        this.productService=productService;
    }
    @PostMapping("/add")
    public Product addProduct(@RequestBody Product product){
        return productService.addProduct(product);
    }
    @GetMapping("/list")
    public List<Product> getProducts(){
        return productService.getProductsByUserId();
    }
    @GetMapping("/{productId}")
    public Product getProduct(@PathVariable int productId){
        return productService.getProductById(productId);
    }
    @PutMapping("/update/{productId}")
    public Product updateProduct(@PathVariable int productId, @RequestBody ProductDTO productDTO){
        return productService.updateProduct(productId, productDTO);
    }
    @DeleteMapping("/delete/{productId}")
    public void deleteProduct(@PathVariable int productId){
        productService.deleteProduct(productId);
    }



}
