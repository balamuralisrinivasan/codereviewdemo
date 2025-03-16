package com.example.inventorymanagement.api;

import com.example.inventorymanagement.model.Product;
import com.example.inventorymanagement.model.Order;
import com.example.inventorymanagement.model.OrderItem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test data builder to create test fixtures for API tests.
 * This class provides methods to create sample products and orders for testing.
 */
public class TestDataBuilder {

    /**
     * Creates a sample product with the given ID.
     */
    public static Product createSampleProduct(Long id) {
        Product product = new Product();
        // Don't set the ID - let the database handle it
        product.name = "Test Product " + (id != null ? id : "");
        product.description = "Test Description for product " + (id != null ? id : "");
        product.price = 99.99;
        product.quantity = 10;
        product.category = "ELECTRONICS";
        product.createdAt = LocalDateTime.now();
        product.updatedAt = LocalDateTime.now();
        return product;
    }
    
    /**
     * Creates a list of sample products.
     */
    public static List<Product> createSampleProducts(int count) {
        List<Product> products = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            products.add(createSampleProduct((long) i));
        }
        return products;
    }
    
    /**
     * Creates a sample product with low stock.
     */
    public static Product createLowStockProduct(Long id) {
        Product product = createSampleProduct(id);
        product.quantity = 2;
        return product;
    }
    
    /**
     * Creates a sample order with the given ID.
     */
    public static Order createSampleOrder(Long id) {
        Order order = new Order();
        order.id = id;
        order.customerName = "Test Customer";
        order.customerEmail = "test@example.com";
        order.status = "PENDING";
        order.orderDate = LocalDateTime.now();
        order.totalAmount = 299.97; // 2 * 99.99 + 1 * 99.99
        order.items = Arrays.asList(
            createOrderItem(1L, 2),
            createOrderItem(2L, 1)
        );
        return order;
    }
    
    /**
     * Creates a sample order item.
     */
    private static OrderItem createOrderItem(Long productId, int quantity) {
        OrderItem item = new OrderItem();
        Product product = createSampleProduct(productId);
        item.product = product;
        item.quantity = quantity;
        item.price = product.price;
        return item;
    }
} 