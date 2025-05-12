package com.example.notificationApp.service;

import com.example.notificationApp.entity.Category;
import com.example.notificationApp.entity.Product;
import com.example.notificationApp.entity.RecommendedProduct;
import com.example.notificationApp.entity.User;
import com.example.notificationApp.mapper.CategoryMapper;
import com.example.notificationApp.repository.CategoryRepository;
import com.example.notificationApp.repository.ProductRepository;
import com.example.notificationApp.repository.RecommendedProductRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private ProductRepository productRepository;
    private UserService userService;
    private RecommendedProductRepository recommendedProductRepository;
    private CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, UserService userService, RecommendedProductRepository recommendedProductRepository, CategoryRepository categoryRepository){
        this.productRepository=productRepository;
        this.userService=userService;
        this.recommendedProductRepository=recommendedProductRepository;
        this.categoryRepository=categoryRepository;
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
  /*  public Product updateProduct(int productId, ProductDTO productDTO){
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
    } */

    public void deleteProduct(int productId){
        User user = userService.getAuthenticatedUser();
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        if (user.getId() != product.getUser().getId()){
            throw new RuntimeException("You are not authorized for delete this product");
        }
        productRepository.deleteById(product.getId());
    }

    public void saveRecommendedProducts(Product originalProduct, List<Integer> recommendedProductIds){
        for (Integer recommendedId: recommendedProductIds){
            Product recommendedProduct = productRepository.findById(recommendedId)
                    .orElseThrow(()-> new RuntimeException("Product not found: "+ recommendedId));
            RecommendedProduct recommendedProductEntry = new RecommendedProduct(originalProduct, recommendedProduct);
            recommendedProductRepository.save(recommendedProductEntry);
        }
    }
    public List<Product> getRecommendedProducts(int productId){

        Product originalProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        User user = userService.getAuthenticatedUser();
        List<RecommendedProduct> recommendedProducts = recommendedProductRepository.findByOriginalProduct(originalProduct);

        return recommendedProducts.stream()
                .map(RecommendedProduct::getRecommendedProduct)
                .collect(Collectors.toList());
    }
    //web scraping method for productList
    @Scheduled(fixedRate = 10800000) //milisaniye 1000*60*60*3 (a per 3 hours)
    public List<Map<String, String>> getAllProductDetails(){
        List<Product> products = productRepository.findAll();
        RestTemplate restTemplate = new RestTemplate();
        List<Map<String, String>> productDetailsList = new ArrayList<>();

        for (Product product: products){
            System.out.println("Processing product: " + product.getLink()); // Log ekleyelim

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

                    String productName = response.get("name");
                    String productPrice = response.get("price");
                    String originalPrice = response.get("originalPrice");
                    String productImage = response.get("imageUrl");
                    String productDescription = response.get("description");
                    String productCategoryName = response.get("productCategory");
                    String mappedCategoryName = CategoryMapper.mapCategoryByName(productCategoryName);
                    Category category = categoryRepository.findByName(mappedCategoryName)
                            .orElseGet(()->{
                                Category newCategory = new Category();
                                newCategory.setName(mappedCategoryName);
                                return categoryRepository.save(newCategory);
                            });
                    product.setCategory(category);
                    LocalDateTime scrapedTime = LocalDateTime.now();


                    response.put("scrapedAt", scrapedTime.toString());
                    String inStock = response.get("stock");
                    product.setDescription(productDescription);
                    product.setImageUrl(productImage);
                    product.setProductName(productName);
                    product.setProductPrice(productPrice);
                    product.setOriginalPrice(originalPrice);
                    product.setScrapedAt(scrapedTime);
                    product.setInStock(inStock);
                    productRepository.save(product);

                    productDetailsList.add(response);
                    System.out.println("Scraped product: " + response);
                }
            } catch (Exception e){
                System.out.println("Error scraping product: "+ product.getId());
            }
        }
        return productDetailsList;
    }
    //scrape method for product
    public Map<String, String> scrapeProduct(int productId){
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        System.out.println("Processing product: "+ product.getLink());
        String productLink = product.getLink();
        if (productLink == null || productLink.isEmpty()){
            System.out.println("Skipping product: No link found");
            throw  new RuntimeException("Product link is missing");
        }
        String flaskApiUrl = "http://127.0.0.1:5000/scrape?url=" + productLink;
        RestTemplate restTemplate = new RestTemplate();

        try{
            Map<String, String> response = restTemplate.getForObject(flaskApiUrl, HashMap.class);
            if (response != null){
                response.put("productId", String.valueOf(product.getId()));
                response.put("productLink", product.getLink());
                LocalDateTime scrapedTime = LocalDateTime.now();
                response.put("scrapedAt", scrapedTime.toString());
                String productName = response.get("name");
                String productPrice = response.get("price");
                String originalPrice = response.get("originalPrice");
                String isInStock = response.get("stock");
                String productImage = response.get("imageUrl");
                String productDescription = response.get("description");
                String productCategoryName = response.get("productCategory");
                String mappedCategoryName = CategoryMapper.mapCategoryByName(productCategoryName);
                Category category = categoryRepository.findByName(mappedCategoryName)
                        .orElseGet(()->{
                            Category newCategory = new Category();
                            newCategory.setName(mappedCategoryName);
                            return categoryRepository.save(newCategory);
                        });
                product.setCategory(category);

                product.setDescription(productDescription);
                product.setImageUrl(productImage);
                product.setProductName(productName);
                product.setProductPrice(productPrice);
                product.setScrapedAt(scrapedTime);
                product.setOriginalPrice(originalPrice);

                product.setInStock(isInStock);
                productRepository.save(product);

                System.out.println("Scraped product: "+ response);
                return response;
            }else{
                throw new RuntimeException("Scraping API returned null response");
            }
        }catch (Exception e){
            System.out.println("Error scraping product: "+productId);
            throw new RuntimeException("Scraping failed for product: "+ productId, e);
        }
    }
    //python tarafında matris icin döndürecegimiz product list
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public List<Product>  getRecommendedProductsForUser() {
        User currentUser = userService.getAuthenticatedUser();
        List<Product> userProducts = productRepository.findAllByUserId(currentUser.getId());

        Set<Integer> addedProductIds = new HashSet<>();
        List<Product> allRecommendedProducts = new ArrayList<>();

        for (Product originalProduct : userProducts){
            List<RecommendedProduct> recommendations = recommendedProductRepository.findByOriginalProduct(originalProduct);

            for (RecommendedProduct rp : recommendations){
                Product recommended = rp.getRecommendedProduct();
                if (!addedProductIds.contains(recommended.getId())){
                    allRecommendedProducts.add(recommended);
                    addedProductIds.add(recommended.getId());
                }
            }
        }
        return allRecommendedProducts;
    }
/*
    public List<Product> getProductsByCommonCategory(String commonCategoryName){
        List<Category> allCategories = categoryRepository.findAll();

        List<Integer> matchingCategoryIds = allCategories.stream()
                .filter(category -> category.getName().toLowerCase().contains(commonCategoryName.toLowerCase()))
                .map(Category::getId)
                .collect(Collectors.toList());
        if (matchingCategoryIds.isEmpty()){
            return new ArrayList<>();
        }
        return productRepository.findAll().stream()
                .filter(product -> matchingCategoryIds.contains(product.getCategory().getId()))
                .collect(Collectors.toList());
    }*/
    public List<Product> searchProductByName(String query){
        return productRepository.findAll().stream()
                .filter(product -> product.getProductName() != null && product.getProductName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }
    public List<Product> getProductsByCategory(String categoryName){
        return productRepository.findAll().stream()
                .filter(p -> p.getCategory() != null &&
                        p.getCategory().getName() != null &&
                        p.getCategory().getName().equalsIgnoreCase(categoryName))
                .collect(Collectors.toList());
    }

    public List<Product> getDiscountedProducts(){
        return productRepository.findProductByOriginalPriceNot("Fiyat bilgisi yok");
    }
}
