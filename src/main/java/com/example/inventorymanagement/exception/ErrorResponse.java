package com.example.inventorymanagement.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    public int status;
    public String message;
    public LocalDateTime timestamp;
} 