package com.example.inventorymanagement.utils;

import com.example.inventorymanagement.model.Product;
import com.example.inventorymanagement.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Arrays;

@Configuration
public class DBInit {
    
    private static final Logger log = LoggerFactory.getLogger(DBInit.class);

    @Bean
    CommandLineRunner initDatabase(ProductRepository repository) {
        return args -> {
            log.info("Preloading database with sample products");
            
            if (repository.count() == 0) {
                Product laptop = new Product();
                laptop.name = "Laptop";
                laptop.description = "High-performance laptop with 16GB RAM and 512GB SSD";
                laptop.price = 1299.99;
                laptop.quantity = 10;
                laptop.category = "Electronics";
                laptop.createdAt = LocalDateTime.now();
                laptop.updatedAt = LocalDateTime.now();
                
                Product smartphone = new Product();
                smartphone.name = "Smartphone";
                smartphone.description = "Latest model with 128GB storage and 5G capability";
                smartphone.price = 899.99;
                smartphone.quantity = 15;
                smartphone.category = "Electronics";
                smartphone.createdAt = LocalDateTime.now();
                smartphone.updatedAt = LocalDateTime.now();
                
                Product headphones = new Product();
                headphones.name = "Headphones";
                headphones.description = "Noise-cancelling wireless headphones with 30-hour battery life";
                headphones.price = 249.99;
                headphones.quantity = 20;
                headphones.category = "Accessories";
                headphones.createdAt = LocalDateTime.now();
                headphones.updatedAt = LocalDateTime.now();
                
                Product monitor = new Product();
                monitor.name = "Monitor";
                monitor.description = "27-inch 4K monitor";
                monitor.price = 350.00;
                monitor.quantity = 8;
                monitor.category = "Electronics";
                monitor.createdAt = LocalDateTime.now();
                monitor.updatedAt = LocalDateTime.now();
                
                Product keyboard = new Product();
                keyboard.name = "Keyboard";
                keyboard.description = "Mechanical gaming keyboard";
                keyboard.price = 120.00;
                keyboard.quantity = 25;
                keyboard.category = "Accessories";
                keyboard.createdAt = LocalDateTime.now();
                keyboard.updatedAt = LocalDateTime.now();
                
                repository.saveAll(Arrays.asList(laptop, smartphone, headphones, monitor, keyboard));
                log.info("Database initialized with 5 products");
            }
        };
    }
} 