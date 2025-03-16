package com.example.inventorymanagement.contract;

import com.example.inventorymanagement.api.TestConfig;
import com.example.inventorymanagement.controller.ProductController;
import com.example.inventorymanagement.service.ProductService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.WebApplicationContext;

/**
 * Base class for Product API contract tests.
 * This class sets up the environment for contract tests.
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMessageVerifier
public class ProductContractBase {

    @Autowired
    private WebApplicationContext context;
    
    @Autowired
    private ProductController productController;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private TestConfig.TestDataInitializer testDataInitializer;

    @BeforeEach
    public void setup() {
        // Initialize test data
        testDataInitializer.initializeProducts();
        
        // Set up RestAssured for contract tests
        RestAssuredMockMvc.webAppContextSetup(context);
    }
    
    public Long getFirstProductId() {
        return testDataInitializer.getProductId("Test Product 1");
    }
} 