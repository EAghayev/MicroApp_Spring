package com.microapp.inventoryservice.service;


import com.microapp.inventoryservice.dto.InventoryCreateDto;
import com.microapp.inventoryservice.dto.InventoryStatusGetDto;
import com.microapp.inventoryservice.model.Inventory;
import com.microapp.inventoryservice.repository.InventoryJpaRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;


public class InventoryServiceTest {

    private InventoryService inventoryService;
    private InventoryJpaRepository inventoryJpaRepository;
    @Before
    public void setUp() throws Exception {

        inventoryJpaRepository = Mockito.mock(InventoryJpaRepository.class);
        inventoryService = new InventoryService(inventoryJpaRepository);

    }

    @Test
    public void whenCreateInventory_itShouldReturnInventory(){

        InventoryCreateDto createDto = InventoryCreateDto.builder()
                .skuCode("test_pr_1")
                .quantity(1).build();

        Inventory entity = Inventory.builder()
                .skuCode(createDto.getSkuCode())
                .quantity(createDto.getQuantity()).build();

        InventoryStatusGetDto getDto = InventoryStatusGetDto.builder()
                .skuCode(entity.getSkuCode())
                .isInStock(entity.getQuantity()>0).build();

        Mockito.when(inventoryJpaRepository.existsBySkuCode("test_pr_1")).thenReturn(false);
        Mockito.when(inventoryJpaRepository.save(entity)).thenReturn(entity);

        InventoryStatusGetDto result = inventoryService.create(createDto);

        Assert.assertEquals(result,getDto);
        Mockito.verify(inventoryJpaRepository).existsBySkuCode("test_pr_1");
        Mockito.verify(inventoryJpaRepository).save(entity);
    }
}