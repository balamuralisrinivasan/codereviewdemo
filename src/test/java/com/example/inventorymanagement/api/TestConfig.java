package com.example.inventorymanagement.api;

import com.example.inventorymanagement.model.Product;
import com.example.inventorymanagement.repository.ProductRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Test configuration for API tests.
 * This class provides beans for test setup and data initialization.
 */
@TestConfiguration
@Profile("test")
public class TestConfig {

    /**
     * Bean to initialize test data in the repository.
     */
    @Bean
    @Primary
    public TestDataInitializer testDataInitializer(ProductRepository productRepository) {
        return new TestDataInitializer(productRepository);
    }

    /**
     * Inner class to initialize test data.
     */
    public static class TestDataInitializer {
        private final ProductRepository productRepository;
        private final Map<String, Long> productIdMap = new ConcurrentHashMap<>();

        public TestDataInitializer(ProductRepository productRepository) {
            this.productRepository = productRepository;
        }

        /**
         * Initializes the database with test products.
         */
        public void initializeProducts() {
            // Clear existing data
            productRepository.deleteAll();
            productIdMap.clear();
            
            // Create and save test products
            List<Product> products = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                Product product = TestDataBuilder.createSampleProduct((long) i);
                Product savedProduct = productRepository.save(product);
                products.add(savedProduct);
                productIdMap.put(savedProduct.name, savedProduct.id);
            }
            
            // Add a low stock product
            Product lowStockProduct = TestDataBuilder.createLowStockProduct(6L);
            Product savedLowStockProduct = productRepository.save(lowStockProduct);
            products.add(savedLowStockProduct);
            productIdMap.put(savedLowStockProduct.name, savedLowStockProduct.id);
            
            // Verify products were saved with IDs
            for (Product product : products) {
                if (product.id == null) {
                    throw new IllegalStateException("Product was not saved with an ID: " + product.name);
                }
            }
        }
        
        /**
         * Gets the ID of a saved product by its name.
         */
        public Long getProductId(String productName) {
            return productIdMap.get(productName);
        }
    }
} 