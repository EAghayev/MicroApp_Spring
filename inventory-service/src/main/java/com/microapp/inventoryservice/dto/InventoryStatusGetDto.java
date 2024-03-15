package com.microapp.inventoryservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InventoryStatusGetDto {
    private String skuCode;
    private boolean isInStock;
}
