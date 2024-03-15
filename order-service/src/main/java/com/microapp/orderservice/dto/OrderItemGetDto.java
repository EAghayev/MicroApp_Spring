package com.microapp.orderservice.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItemGetDto {
    private Long id;
    private String skuCode;
    private BigDecimal price;
    private String product;
    private Integer quantity;
}
