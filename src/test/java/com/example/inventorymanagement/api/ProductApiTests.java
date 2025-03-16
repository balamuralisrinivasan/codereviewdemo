package com.example.inventorymanagement.api;

import com.example.inventorymanagement.model.Product;
import com.example.inventorymanagement.repository.ProductRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * API tests for the Product controller.
 * These tests verify the behavior of the Product API endpoints.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestConfig.class)
public class ProductApiTests {

    @LocalServerPort
    private int port;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestConfig.TestDataInitializer testDataInitializer;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
        RestAssured.basePath = "/api";
        
        // Initialize test data
        testDataInitializer.initializeProducts();
    }

    @Test
    public void testGetAllProducts() {
        given()
            .when()
            .get("/products")
            .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(5));
    }

    @Test
    public void testGetProductById() {
        Long productId = testDataInitializer.getProductId("Test Product 1");
        
        given()
            .when()
            .get("/products/{id}", productId)
            .then()
            .statusCode(200)
            .body("id", equalTo(productId.intValue()))
            .body("name", equalTo("Test Product 1"))
            .body("category", equalTo("ELECTRONICS"));
    }

    @Test
    public void testCreateProduct() {
        Product newProduct = TestDataBuilder.createSampleProduct(null);
        newProduct.name = "New Test Product";
        
        given()
            .contentType(ContentType.JSON)
            .body(newProduct)
            .when()
            .post("/products")
            .then()
            .statusCode(201)
            .body("name", equalTo("New Test Product"))
            .body("id", notNullValue());
    }

    @Test
    public void testUpdateProduct() {
        Long productId = testDataInitializer.getProductId("Test Product 1");
        Product product = productRepository.findById(productId).orElseThrow();
        product.name = "Updated Product Name";
        
        given()
            .contentType(ContentType.JSON)
            .body(product)
            .when()
            .put("/products/{id}", productId)
            .then()
            .statusCode(200)
            .body("name", equalTo("Updated Product Name"))
            .body("id", equalTo(productId.intValue()));
    }

    @Test
    public void testDeleteProduct() {
        Long productId = testDataInitializer.getProductId("Test Product 1");
        
        given()
            .when()
            .delete("/products/{id}", productId)
            .then()
            .statusCode(204);
        
        // Verify product is deleted
        given()
            .when()
            .get("/products/{id}", productId)
            .then()
            .statusCode(404);
    }

    @Test
    public void testSearchProductsByName() {
        given()
            .param("name", "Test Product")
            .when()
            .get("/products/search")
            .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(1))
            .body("[0].name", containsString("Test Product"));
    }

    @Test
    public void testFindProductsByCategory() {
        given()
            .when()
            .get("/products/category/{category}", "ELECTRONICS")
            .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(1))
            .body("[0].category", equalTo("ELECTRONICS"));
    }

    @Test
    public void testFindLowStockProducts() {
        given()
            .param("threshold", 5)
            .when()
            .get("/products/low-stock")
            .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(1))
            .body("[0].quantity", lessThanOrEqualTo(5));
    }
} 