package com.example.inventorymanagement.api;

import com.example.inventorymanagement.model.Product;
import com.example.inventorymanagement.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Simple test for the Product repository.
 * This test verifies basic CRUD operations on the Product entity.
 */
@SpringBootTest
@ActiveProfiles("test")
public class SimpleProductTest {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void setup() {
        // Clear existing data
        productRepository.deleteAll();
        
        // Create and save test products
        Product product = new Product();
        product.name = "Test Product";
        product.description = "Test Description";
        product.price = 99.99;
        product.quantity = 10;
        product.category = "ELECTRONICS";
        product.createdAt = LocalDateTime.now();
        product.updatedAt = LocalDateTime.now();
        
        productRepository.save(product);
    }

    @Test
    public void testFindAllProducts() {
        List<Product> products = productRepository.findAll();
        
        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("Test Product", products.get(0).name);
    }
    
    @Test
    public void testCreateProduct() {
        Product product = new Product();
        product.name = "Another Product";
        product.description = "Another Description";
        product.price = 49.99;
        product.quantity = 5;
        product.category = "BOOKS";
        product.createdAt = LocalDateTime.now();
        product.updatedAt = LocalDateTime.now();
        
        Product savedProduct = productRepository.save(product);
        
        assertNotNull(savedProduct.id);
        assertEquals("Another Product", savedProduct.name);
        
        List<Product> products = productRepository.findAll();
        assertEquals(2, products.size());
    }
} 