package com.ecommerce.app.config;

import com.ecommerce.app.entity.Product;
import com.ecommerce.app.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private ProductService productService;
    
    @Override
    public void run(String... args) throws Exception {
        // Add sample products if none exist
        if (productService.getAllProducts().isEmpty()) {
            productService.saveProduct(new Product(
                "Laptop", 
                "High-performance laptop with latest processor", 
                new BigDecimal("999.99"), 
                "https://cdn.thewirecutter.com/wp-content/media/2024/11/cheapgaminglaptops-2048px-7981.jpg?auto=webp&quality=75&width=1024", 
                10
            ));
            
            productService.saveProduct(new Product(
                "Smartphone", 
                "Latest smartphone with advanced camera", 
                new BigDecimal("699.99"), 
                "https://www.apple.com/v/iphone/home/cd/images/meta/iphone__kqge21l9n26q_og.png", 
                15
            ));
            
            productService.saveProduct(new Product(
                "Headphones", 
                "Wireless noise-cancelling headphones", 
                new BigDecimal("199.99"), 
                "https://cdn.mos.cms.futurecdn.net/PbBRJvxoAm4BM7vfhh8ZfG.jpg", 
                20
            ));
            
            productService.saveProduct(new Product(
                "Tablet", 
                "10-inch tablet perfect for work and entertainment", 
                new BigDecimal("399.99"), 
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRauO_3jTuSfQcZmQvQrrrW5ZT8GYDHcZRzug&s", 
                8
            ));
            
            productService.saveProduct(new Product(
                "Smartwatch", 
                "Fitness tracking smartwatch with heart rate monitor", 
                new BigDecimal("299.99"), 
                "https://cdn.thewirecutter.com/wp-content/media/2023/11/fitness-tracker-2048px-5346.jpg?auto=webp&quality=75&crop=1.91:1&width=1200", 
                12
            ));
        }
    }
} 