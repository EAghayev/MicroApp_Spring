package com.microapp.orderservice.client;

import com.microapp.orderservice.dto.InventoryStatusResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name="inventoryClient",url="http://localhost:8080/inventories")
public interface InventoryClient {
    @GetMapping("")
    InventoryStatusResponseDto[] getAll(@RequestParam  List<String> skuCode);
}
