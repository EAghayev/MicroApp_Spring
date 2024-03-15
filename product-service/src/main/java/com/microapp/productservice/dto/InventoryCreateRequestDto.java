package com.microapp.productservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InventoryCreateRequestDto {
    private String skuCode;
    private int quantity;
}
