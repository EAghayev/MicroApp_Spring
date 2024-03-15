package com.microapp.orderservice.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductResponseDto {
    private Long id;
    private String name;
    private String skuCode;
    private BigDecimal salePrice;
    private BigDecimal costPrice;
}
