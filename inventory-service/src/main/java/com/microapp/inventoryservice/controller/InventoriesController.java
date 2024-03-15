package com.microapp.inventoryservice.controller;

import com.microapp.inventoryservice.dto.InventoryCreateDto;
import com.microapp.inventoryservice.dto.InventoryStatusGetDto;
import com.microapp.inventoryservice.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventories")
@RequiredArgsConstructor
public class InventoriesController {

    private final InventoryService inventoryService;

    @GetMapping("/{skuCode}")
    public boolean isInStock(@PathVariable String skuCode){
        return inventoryService.isInStock(skuCode);
    }

    @GetMapping
    public ResponseEntity<List<InventoryStatusGetDto>> isInStock(@RequestParam List<String> skuCode){
        var data =  inventoryService.getStockStatusBySkuCodes(skuCode);

        return  ResponseEntity.ok(data);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid InventoryCreateDto createDto){
        inventoryService.create(createDto);
        return ResponseEntity.status(201).build();
    }

}
