package com.microapp.orderservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemCreateDto {
    private Long productId;
    private Integer quantity;
}
