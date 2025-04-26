package com.example.notificationApp.controller;

import com.example.notificationApp.entity.Category;
import com.example.notificationApp.entity.Product;
import com.example.notificationApp.entity.User;
import com.example.notificationApp.service.ProductService;
import com.example.notificationApp.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {

    private ProductService productService;
    private UserService userService;
    public ProductController(ProductService productService, UserService userService){
        this.productService=productService;
        this.userService=userService;
    }
    @PostMapping("/add")
    public Product addProduct(@RequestBody Product product){
        Product savedProduct = productService.addProduct(product);
        try{
            Map<String, String> scrapedData = productService.scrapeProduct(savedProduct.getId());
            System.out.println("Scraping completed for new product: "+scrapedData);
            List<Product> allProducts = productService.getAllProducts();
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("newProduct", savedProduct);
            requestBody.put("allProducts", allProducts);

            String getProductsApiUrl = "http://127.0.0.1:5001/compute_similarity";
            RestTemplate restTemplate = new RestTemplate();
            List<Integer> recommendedProductIds = restTemplate.postForObject(getProductsApiUrl, requestBody, List.class);

            productService.saveRecommendedProducts(savedProduct, recommendedProductIds);
            System.out.println("Recommended products saved: "+recommendedProductIds);
        } catch (Exception e){
            System.err.println("Error in scraping or similarity analysis: "+ e.getMessage());
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
    //python'a döndürecegimiz product list
    @GetMapping("/allFromDB")
    public List<Product> getAllProducts(){
        User user = userService.getUserPrincipal();
        return productService.getAllProducts();
    }
    @GetMapping("/recommended/{productId}")
    public List<Product> getRecommendedProducts(@PathVariable int productId){
        return productService.getRecommendedProducts(productId);
    }
    @GetMapping("/recommended")
    public List<Product> getUserRecommendedProducts(){
        return productService.getRecommendedProductsForUser();
    }
    @GetMapping("/commoncategory/{categoryName}")
    public List<Product> getProductsByCommonCategory(@PathVariable String categoryName){
        return productService.getProductsByCommonCategory(categoryName);
    }

}
