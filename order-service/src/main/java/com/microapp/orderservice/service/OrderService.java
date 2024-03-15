package com.microapp.orderservice.service;

import com.microapp.orderservice.client.InventoryClient;
import com.microapp.orderservice.client.ProductClient;
import com.microapp.orderservice.dto.*;
import com.microapp.orderservice.exception.RestException;
import com.microapp.orderservice.model.Order;
import com.microapp.orderservice.model.OrderItem;
import com.microapp.orderservice.repository.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderJpaRepository orderJpaRepository;
    private final WebClient webClient;
    private final ProductClient productClient;
    private final InventoryClient inventoryClient;
    public OrderGetDto create(OrderCreateDto createDto){
        Order entity = Order.builder()
                .createdAt(LocalDateTime.now())
                .orderNumber(UUID.randomUUID().toString()).build();

        List<OrderItem> items = createDto.getOrderItems().stream().map(this::mapToEntity).toList();
        entity.setOrderItemsList(items);

        List<Long> productIds = items.stream().map(OrderItem::getProductId).toList();

//        ProductResponseDto[] productArr = webClient.get()
//                .uri("http://localhost:8051/api/v1/products/all",
//                        uriBuilder -> uriBuilder.queryParam("id",productIds).build())
//                .retrieve().bodyToMono(ProductResponseDto[].class).block();
        log.info("testing");
        ProductResponseDto[] productArr = productClient.getAll(productIds);

        boolean allProductAreExists = productArr.length==createDto.getOrderItems().stream().count();

        if(!allProductAreExists){
            throw new RestException(HttpStatus.BAD_REQUEST,"Not all products are exist");
        }

        List<String> skuCodes = Arrays.stream(productArr).map(ProductResponseDto::getSkuCode).toList();


//        InventoryStatusResponseDto[] inventoryStatusArr = webClient.get()
//                        .uri("http://localhost:8053/api/v1/inventories",
//                                uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build())
//                        .retrieve().bodyToMono(InventoryStatusResponseDto[].class)
//                        .block();
        InventoryStatusResponseDto[] inventoryStatusArr = inventoryClient.getAll(skuCodes);

        boolean allAreInStock = Arrays.stream(inventoryStatusArr).allMatch(InventoryStatusResponseDto::isInStock);


        if (inventoryStatusArr.length==productArr.length && allAreInStock){
            orderJpaRepository.save(entity);
        }
        else {
            throw new RestException(HttpStatus.BAD_REQUEST,"Not all products is in stock!");
        }

        return mapToDto(entity);
    }


    private OrderItem mapToEntity(OrderItemCreateDto dto){
        return OrderItem.builder()
                .quantity(dto.getQuantity())
                .productId(dto.getProductId()).build();
    }
    private OrderGetDto mapToDto(Order entity){
        return OrderGetDto.builder()
                .id(entity.getId())
                .createdAt(entity.getCreatedAt())
                .orderItems(entity.getOrderItemsList().stream().map(this::mapToDto).toList()).build();
    }
    private OrderItemGetDto mapToDto(OrderItem entity){
        return OrderItemGetDto.builder()
                .id(entity.getId())
                .price(entity.getPrice())
                .quantity(entity.getQuantity()).build();
    }

}
