package com.microapp.orderservice.dto;

import lombok.Data;

@Data
public class InventoryStatusResponseDto {
    private String skuCode;
    private boolean isInStock;
}
