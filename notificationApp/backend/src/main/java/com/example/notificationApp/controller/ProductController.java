package com.example.notificationApp.controller;

import com.example.notificationApp.entity.Product;
import com.example.notificationApp.model.ProductDTO;
import com.example.notificationApp.service.ProductService;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.PanelUI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {

    private ProductService productService;
    public ProductController(ProductService productService){
        this.productService=productService;
    }
    @PostMapping("/add")
    public Product addProduct(@RequestBody Product product){
        Product savedProduct = productService.addProduct(product);
        try{
            Map<String, String> scrapedData = productService.scrapeProduct(savedProduct.getId());
            System.out.println("Scraping completed for new product: "+scrapedData);
        } catch (Exception e){
            System.err.println("Scraping failed for new product: "+ e.getMessage());
        }
        return savedProduct;
    }
    @GetMapping("/list")
    public List<Product> getProducts(){
        return productService.getProductsByUserId();
    }
    @GetMapping("/{productId}")
    public Product getProduct(@PathVariable int productId){
        return productService.getProductById(productId);
    }
   /* @PutMapping("/update/{productId}")
    public Product updateProduct(@PathVariable int productId, @RequestBody ProductDTO productDTO){
        return productService.updateProduct(productId, productDTO);
    } */
    @DeleteMapping("/delete/{productId}")
    public void deleteProduct(@PathVariable int productId){
        productService.deleteProduct(productId);
    }

    //web scraping
    @GetMapping("/scrape/all")
    public List<Map<String, String>> scrapeAllProducts(){
        return productService.getAllProductDetails();
    }
}
