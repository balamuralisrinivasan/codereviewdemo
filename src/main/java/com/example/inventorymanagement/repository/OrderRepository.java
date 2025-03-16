package com.example.inventorymanagement.repository;

import com.example.inventorymanagement.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(String status);
    
    List<Order> findByCustomerEmail(String email);
    
    List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
} 