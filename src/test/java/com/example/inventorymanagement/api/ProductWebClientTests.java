package com.example.inventorymanagement.api;

import com.example.inventorymanagement.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.context.annotation.Import;

/**
 * WebTestClient tests for the Product API.
 * These tests use WebTestClient which will work with both servlet and reactive endpoints.
 * This prepares us for the future migration to reactive endpoints.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestConfig.class)
public class ProductWebClientTests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private TestConfig.TestDataInitializer testDataInitializer;

    @BeforeEach
    public void setup() {
        // Initialize test data
        testDataInitializer.initializeProducts();
    }

    @Test
    public void testGetAllProducts() {
        webTestClient
            .get()
            .uri("/api/products")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(Product.class)
            .hasSize(6);
    }

    @Test
    public void testGetProductById() {
        Long productId = testDataInitializer.getProductId("Test Product 1");
        
        webTestClient
            .get()
            .uri("/api/products/{id}", productId)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.id").isEqualTo(productId)
            .jsonPath("$.name").isEqualTo("Test Product 1")
            .jsonPath("$.category").isEqualTo("ELECTRONICS");
    }

    @Test
    public void testCreateProduct() {
        Product newProduct = TestDataBuilder.createSampleProduct(null);
        newProduct.name = "New Test Product";
        
        webTestClient
            .post()
            .uri("/api/products")
            .bodyValue(newProduct)
            .exchange()
            .expectStatus().isCreated()
            .expectBody()
            .jsonPath("$.name").isEqualTo("New Test Product")
            .jsonPath("$.id").isNotEmpty();
    }

    @Test
    public void testUpdateProduct() {
        Long productId = testDataInitializer.getProductId("Test Product 1");
        Product product = TestDataBuilder.createSampleProduct(productId);
        product.name = "Updated Product Name";
        
        webTestClient
            .put()
            .uri("/api/products/{id}", productId)
            .bodyValue(product)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.name").isEqualTo("Updated Product Name")
            .jsonPath("$.id").isEqualTo(productId);
    }

    @Test
    public void testDeleteProduct() {
        Long productId = testDataInitializer.getProductId("Test Product 1");
        
        webTestClient
            .delete()
            .uri("/api/products/{id}", productId)
            .exchange()
            .expectStatus().isNoContent();
        
        // Verify product is deleted
        webTestClient
            .get()
            .uri("/api/products/{id}", productId)
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    public void testSearchProductsByName() {
        webTestClient
            .get()
            .uri(uriBuilder -> uriBuilder
                .path("/api/products/search")
                .queryParam("name", "Test Product")
                .build())
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(Product.class)
            .hasSize(6);
    }

    @Test
    public void testFindProductsByCategory() {
        webTestClient
            .get()
            .uri("/api/products/category/{category}", "ELECTRONICS")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(Product.class)
            .hasSize(6);
    }

    @Test
    public void testFindLowStockProducts() {
        webTestClient
            .get()
            .uri(uriBuilder -> uriBuilder
                .path("/api/products/low-stock")
                .queryParam("threshold", 5)
                .build())
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(Product.class)
            .hasSize(1);
    }
} 