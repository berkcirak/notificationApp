package com.example.notificationApp.service;

import com.example.notificationApp.entity.Product;
import com.example.notificationApp.entity.User;
import com.example.notificationApp.model.ProductDTO;
import com.example.notificationApp.repository.ProductRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if (productDTO.getDescription() != null){
            product.setDescription(productDTO.getDescription());
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


    //web scraping method
    public List<Map<String, String>> getAllProductDetails(){
        List<Product> products = productRepository.findAll();
        RestTemplate restTemplate = new RestTemplate();
        List<Map<String, String>> productDetailsList = new ArrayList<>();

        for (Product product: products){
            System.out.println("Processing product: " + product.getTitle()); // Log ekleyelim

            String productLink = product.getLink();
            if (productLink == null || productLink.isEmpty()){
                System.out.println("Skipping product: No link found");
                continue;
            }
            String flaskApiUrl = "http://127.0.0.1:5000/scrape?url=" + productLink;
            try {
                Map<String, String> response = restTemplate.getForObject(flaskApiUrl, HashMap.class);
                if (response != null){
                    response.put("productId", String.valueOf(product.getId()));
                    response.put("productLink", String.valueOf(product.getLink()));
                    response.put("productTitle", String.valueOf(product.getTitle()));
                    productDetailsList.add(response);
                    System.out.println("Scraped product: " + response);
                }
            } catch (Exception e){
                System.out.println("Error scraping product: "+ product.getId());
            }
        }
        return productDetailsList;
    }






}
