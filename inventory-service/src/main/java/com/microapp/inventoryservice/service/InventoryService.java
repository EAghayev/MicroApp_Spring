package com.microapp.inventoryservice.service;

import com.microapp.inventoryservice.dto.InventoryCreateDto;
import com.microapp.inventoryservice.dto.InventoryStatusGetDto;
import com.microapp.inventoryservice.exception.RestException;
import com.microapp.inventoryservice.model.Inventory;
import com.microapp.inventoryservice.repository.InventoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryJpaRepository inventoryJpaRepository;

    public boolean isInStock(String skuCode){
        return  inventoryJpaRepository.existsBySkuCode(skuCode);
    }

    public List<InventoryStatusGetDto> getStockStatusBySkuCodes(List<String> skuCodes){
        return inventoryJpaRepository.findBySkuCodeIn(skuCodes).stream().map(this::mapToDto).toList();
    }

    public List<InventoryStatusGetDto> getAll(){
        return inventoryJpaRepository.findAll().stream().map(this::mapToDto).toList();
    }

    public void create(InventoryCreateDto createDto){

        if(inventoryJpaRepository.existsBySkuCode(createDto.getSkuCode())){
            throw new RestException(HttpStatus.BAD_REQUEST,"skuCode","Inventory already exists by given skuCode");
        }

        Inventory entity = Inventory.builder()
                .skuCode(createDto.getSkuCode())
                .quantity(createDto.getQuantity()).build();

        inventoryJpaRepository.save(entity);
    }



    private InventoryStatusGetDto mapToDto(Inventory entity){
        return InventoryStatusGetDto.builder()
                .skuCode(entity.getSkuCode())
                .isInStock(entity.getQuantity()>0).build();
    }
}
