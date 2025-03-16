package com.example.inventorymanagement.service;

import com.example.inventorymanagement.model.Order;
import com.example.inventorymanagement.model.OrderItem;
import com.example.inventorymanagement.model.Product;
import com.example.inventorymanagement.repository.OrderRepository;
import com.example.inventorymanagement.repository.ProductRepository;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    
    @Autowired
    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }
    
    @Transactional
    public Order createOrder(Order order) {
        // Calculate total amount
        double total = 0;
        
        for (OrderItem item : order.items) {
            Product product = productRepository.findById(item.product.id)
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + item.product.id));
            
            // Check if enough stock is available
            if (product.quantity < item.quantity) {
                throw new IllegalStateException("Not enough stock for product: " + product.name);
            }
            
            // Update inventory
            product.quantity = product.quantity - item.quantity;
            productRepository.save(product);
            
            // Set the product and calculate line total
            item.product = product;
            item.price = product.price;
            total += item.price * item.quantity;
        }
        
        // Set order date and status if not already set
        if (order.orderDate == null) {
            order.orderDate = LocalDateTime.now();
        }
        
        if (order.status == null) {
            order.status = "NEW";
        }
        
        order.totalAmount = total;
        
        return orderRepository.save(order);
    }
    
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    public Order getOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
    }
    
    @Transactional
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new EntityNotFoundException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }
    
    @Transactional
    public Order updateOrderStatus(Long id, String status) {
        Order order = getOrder(id);
        order.status = status;
        return orderRepository.save(order);
    }
    
    public List<Order> findByStatus(String status) {
        return orderRepository.findByStatus(status);
    }
    
    public List<Order> findByCustomerEmail(String email) {
        return orderRepository.findByCustomerEmail(email);
    }
    
    public List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByOrderDateBetween(startDate, endDate);
    }
} 